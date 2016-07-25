package com.jymf.main;
import com.jymf.bean.json.UploadDownJson;
import com.jymf.bean.packet.BasePackage;
import com.jymf.exception.CrcCheckErrorException;
import com.jymf.exception.IllegalPackageException;
import com.jymf.service.Handler;
import com.jymf.tool.JsonTool;
import com.jymf.tool.MyLogger;
import com.jymf.tool.PacketTool;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.zeromq.ZMQ;

/**
 * 服务主程序,main函数所在类.
 * @author Zhang
 * @version 0.1
 */
public class ServerService {

    static {
        PropertyConfigurator.configure("log4j.properties");  //打包用
//        PropertyConfigurator.configure(ServerService.class.getResource("/").getFile()+"log4j.properties");  //本机测试用
    }

    //程序入口
    public static void main(String[] args) {
        final ZMQ.Context context = ZMQ.context(1);

        //采用ZMQ的router/dealer模式,多线程处理请求
        ZMQ.Socket router = context.socket(ZMQ.ROUTER);
        ZMQ.Socket dealer = context.socket(ZMQ.DEALER);

        router.bind("tcp://*:5555");
        dealer.bind("tcp://*:5556");

        //共创造10个dealer线程
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable(){
                public void run() {
                    ZMQ.Socket response = context.socket(ZMQ.REP);
                    MyLogger logger = new MyLogger(Logger.getRootLogger());

                    response.connect("tcp://*:5556");

                    while (!Thread.currentThread().isInterrupted()) {
                        logger.info("waitting for data");
                        byte[] requestBytes = response.recv();
                        logger.info("接到数据,长度"+requestBytes.length);
                        //构建基包,做初步处理
                        BasePackage basePackage = new BasePackage(requestBytes);
                        int statusCode;
                        String errorMsg;
                        try {
                            //初步验证处理
                            basePackage.check();
                            basePackage.prepare();
                        } catch (IllegalPackageException e) {
                            statusCode = 1;
                            errorMsg = e.getMessage();
                            logger.warn("SC:"+statusCode+" "+errorMsg);
                            e.printStackTrace();
                            //返回一个空的结果,否则这个处理线程会阻塞在这里
                            response.send(new byte[]{});
                            continue;
                        } catch (CrcCheckErrorException e) {
                            statusCode = 2;
                            errorMsg = e.getMessage();
                            logger.warn("SC:"+statusCode+" "+errorMsg);
                            e.printStackTrace();
                            response.send(new byte[]{});
                            continue;
                        } catch (Exception e) {
                            statusCode = 3;
                            errorMsg = e.getMessage();
                            logger.warn("SC:"+statusCode+" "+errorMsg);
                            e.printStackTrace();
                            String jsonString = JsonTool.toJson(new UploadDownJson(statusCode,errorMsg));
                            byte[] respContent = PacketTool.pack(basePackage.getCopeCode(),jsonString,null);
                            response.send(respContent);
                            continue;
                        }
                        try {
                            //根据basePackage中处理得到的copCode选择相应的handler
                            Handler handler = basePackage.selectHandler();
                            handler.handle();
                            //将结果用PacketTool进行打包,组成可以返回的byte[]
                            byte[] respContent = PacketTool.pack(basePackage.getCopeCode(),handler.getJsonString(),handler.getContent());
                            logger.info("返回数据  长度:"+respContent.length);
                            response.send(respContent);
                        } catch (Exception e) {
                            logger.warn("发生其他其他未知错误");
                            continue;
                        }
                    }
                    response.close();
                }

            }).start();
        }
        ZMQ.proxy(router, dealer, null);
        router.close();
        dealer.close();
        context.term();
    }
}
