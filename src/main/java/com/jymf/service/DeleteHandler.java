package com.jymf.service;

import com.google.gson.JsonSyntaxException;
import com.jymf.bean.json.*;
import com.jymf.bean.packet.BasePackage;
import com.jymf.service.solr.MySolrClient;
import com.jymf.tool.JsonTool;
import com.jymf.tool.MyLogger;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import java.io.IOException;

/**
 * 删除处理器类,含有删除操作的主要逻辑
 * @author Zhang
 * @version 0.1
 */
public class DeleteHandler implements Handler {
    private MyLogger logger = new MyLogger(Logger.getRootLogger());

    private BasePackage basePackage = null;
    private int statusCode = 0;
    private String errorMsg = null;
    private byte[] content  = null;

    /**
     * 构造函数
     * @param basePackage BasePackage 经过处理的数据包抽象对象
     */
    public DeleteHandler(BasePackage basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * 对构造函数传入的数据包抽象对象进行删除相关处理
     */
    public void handle() {
        DeleteUpJson json;
        String userName = "";
        MySolrClient client = new MySolrClient();
        try {
            json = (DeleteUpJson) JsonTool.toObject(basePackage.getJsonString(), DeleteUpJson.class);
            if (!json.isComplete()){
                throw new JsonSyntaxException("json数据不完整");
            }
            String itmeId = json.getItemId();
            userName = json.getUsername();
            int order = json.getFileOrder();
            client.deleteByItemIdAndOrder(itmeId,order);
            client.commit();


        } catch (JsonSyntaxException e){
            e.printStackTrace();
            statusCode = 3;
            errorMsg = "Json数据不完整";
        } catch (IOException e) {
            e.printStackTrace();
            statusCode = 5;
            errorMsg = "查询solr索引时发生错误";
        } catch (SolrServerException e) {
            e.printStackTrace();
            statusCode = 5;
            errorMsg = "查询solr索引时发生错误";
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
        DeleteDownJson json = new DeleteDownJson();   //如果没发生异常,则返回一个statusCode为0,errorMsg为""的json对象
        if (statusCode!=0) {
            json.setStatusCode(statusCode);
            json.setErrorMsg(errorMsg);
        }
        return JsonTool.toJson(json);
    }

    /**
     * 获取响应数据包包体的byte[]
     * @return byte[] 响应数据包包体
     */
    public byte[] getContent() {
        return content;
    }
}