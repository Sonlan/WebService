package com.jymf.service;

import com.jymf.bean.json.UploadDownJson;
import com.jymf.bean.json.UploadUpJson;
import com.jymf.bean.packet.BasePackage;
import com.jymf.service.fdfs.FdfsService;
import com.jymf.service.solr.MySolrClient;
import com.jymf.tool.JsonTool;

import com.google.gson.JsonSyntaxException;
import com.jymf.tool.MyLogger;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.csource.common.MyException;

import java.io.IOException;
import java.util.HashMap;

/**
 * 上传处理器类,含有上传操作的主要逻辑
 * @author Zhang
 * @version 0.1
 */
public class UploadHandler implements Handler {

    private MyLogger logger = new MyLogger(Logger.getRootLogger());

    private BasePackage basePackage = null;
    private int statusCode = 0;
    private String errorMsg = null;

    /**
     * 构造函数
     * @param basePackage BasePackage 经过处理的数据包抽象对象
     */
    public UploadHandler(BasePackage basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * 对构造函数传入的数据包抽象对象进行上传相关处理
     */
    public void handle() {
        String fdfsId;
        String userName = "";
        MySolrClient client = new MySolrClient();
        try {
            UploadUpJson json = (UploadUpJson) JsonTool.toObject(basePackage.getJsonString(), UploadUpJson.class);
            if (!json.isComplete()){
                throw new JsonSyntaxException("json数据不完整");
            }
            userName = json.getUsername();
            FdfsService fdfsService = new FdfsService();
            HashMap<String, String> map = new HashMap<String, String>();   // TODO: 15/12/14 随意加入了一个键值对,使用时按需加入
            map.put("author", "jymf");
            fdfsId = fdfsService.upLoadByteArray(basePackage.getOriginalData(), basePackage.getFileDataOffset(), json.getBodyLength(), json.getExtension(), map);
            String fileName = json.getItemId()+"_"+json.getFileOrder();
            client.buildIndex(fileName,fdfsId);
            client.commit();
        } catch (JsonSyntaxException e){
            e.printStackTrace();
            statusCode = 3;
            errorMsg = "数据格式错误:Json数据不完整";
        } catch (IOException e) {
            e.printStackTrace();
            statusCode = 4;
            errorMsg = "文件存储进入dfs时发生错误";
        } catch (MyException e) {
            e.printStackTrace();
            statusCode = 4;
            errorMsg = "文件存储进入dfs时发生错误";
        } catch (SolrServerException e){
            e.printStackTrace();
            statusCode = 5;
            errorMsg = "建立solr索引时发生错误";
        } catch (HttpSolrClient.RemoteSolrException e){
            e.printStackTrace();
            statusCode = 5;
            errorMsg = "查询solr索引时发生错误";
        } finally {
            try {
                client.close();
            } catch (IOException e) {
            }
        }
        if (statusCode!=0){
            logger.warn("User:"+userName+"  SC:"+statusCode+"  "+errorMsg);
        }

    }

    /**
     * 根据获取的状态码和errorMsg组装返回数据包头Json字符串
     * @return String 数据包头Json字符串
     */
    public String getJsonString() {
        UploadDownJson json = new UploadDownJson();   //如果没发生异常,则返回一个statusCode为0,errorMsg为""的json对象
        if (statusCode!=0) {
            logger.warn("SC:"+statusCode+" "+errorMsg);
            json.setStatusCode(statusCode);
            json.setErrorMsg(errorMsg);
        }
        return JsonTool.toJson(json);
    }

    /**
     * 不含数据包体,返回null
     * @return
     */
    public byte[] getContent() {
        return null;
    }
}
