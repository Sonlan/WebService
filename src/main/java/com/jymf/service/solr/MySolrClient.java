package com.jymf.service.solr;

import com.jymf.bean.config.MainConfig;
import com.jymf.bean.json.RespContentJson;
import org.apache.http.impl.execchain.MainClientExec;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;

import java.io.*;
import java.util.Properties;

/**
 * 用于进行建立Solr索引和查询Solr索引的类
 * @author Zhang
 * @version 0.1
 */
public class MySolrClient {

    protected HttpSolrClient client = null;
    protected static String field1 = null;
    protected static String field2 = null;

    /**
     * 读取Solr配置文件,主要是字段名的配置和Solr服务器地址的配置
     */
    static {
        field1 = MainConfig.field1;
        field2 = MainConfig.field2;
    }

    public MySolrClient() {
        this.client = new HttpSolrClient(MainConfig.solrServerUrl);
    }

    /**
     * 建立Solr索引
     * @param fileName String 文件名
     * @param fdfsId String fdfsId
     * @throws IOException
     * @throws SolrServerException
     */
    public void buildIndex(String fileName, String fdfsId) throws IOException,SolrServerException,HttpSolrClient.RemoteSolrException{
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField(field1, fileName);
        solrInputDocument.addField(field2, fdfsId);
        client.add(solrInputDocument);
    }

    /**
     * 根据企业Id或者商品Id查询返回文件下载下行数据包包体的Json封装对象
     * @param itemId String 企业Id或商品Id
     * @return RespContentJson 下载下行数据包包体的Json封装对象
     * @throws IOException
     * @throws SolrServerException
     */
    public RespContentJson queryByItemId(String itemId) throws IOException,SolrServerException,HttpSolrClient.RemoteSolrException{
        SolrParams params = new SolrQuery(field1+":"+itemId+"_*");
        QueryResponse response = client.query(params);
        SolrDocumentList list = response.getResults();

        RespContentJson respContentJson = new RespContentJson();
        respContentJson.setItemId(itemId);
        for (SolrDocument solrDocument : list) {
            String fileName = solrDocument.get(field1).toString();
            String dfsId = solrDocument.get(field2).toString();
            String[] fileNameSep= fileName.split("_");
            int fileOrder = Integer.parseInt(fileNameSep[fileNameSep.length-1]);
            respContentJson.setOneUrl(fileOrder, dfsId);
        }
        return respContentJson;
    }

    /**
     * 根据文件项目Id和文件序删除指定文件
     * @param itemId 文件项目Id
     * @param order 文件序
     * @throws IOException
     * @throws SolrServerException
     * @throws HttpSolrClient.RemoteSolrException
     */
    public void deleteByItemIdAndOrder(String itemId, int order) throws IOException, SolrServerException ,HttpSolrClient.RemoteSolrException{
        String query = field1+":"+itemId+"_"+order;
        client.deleteByQuery(query);

    }

    /**
     * 根据文件项目Id和文件序查找指定文件的Url
     * @param itemId
     * @param order
     * @return
     * @throws IOException
     * @throws SolrServerException
     * @throws HttpSolrClient.RemoteSolrException
     */
    public String queryUrlByItemIdAndOrder(String itemId,int order) throws IOException,SolrServerException,HttpSolrClient.RemoteSolrException{
        SolrParams params = new SolrQuery(field1+":"+itemId+"_"+order);
        QueryResponse response = client.query(params);
        SolrDocumentList list = response.getResults();
        String dfsId = null;
        for (SolrDocument solrDocument : list) {
            dfsId = solrDocument.get(field2).toString();
        }
        return dfsId;
    }

    /**
     * 提交缓存中的数据,真正执行操作(查询类操作不需要)
     * @throws IOException
     * @throws SolrServerException
     */
    public void commit() throws IOException, SolrServerException{
        client.commit();
    }

    /**
     * 关闭Solr客户端
     * @throws IOException
     */
    public  void close() throws IOException{
        client.close();
    }

}
