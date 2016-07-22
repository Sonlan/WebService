package com.jymf.bean.config;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

import java.io.*;
import java.util.Properties;

/**
 * 用于在程序执行之前主配置文件信息的加载工作
 * @author Zhang
 * @version 0.1
 */
public class MainConfig {
    public static String field1 = null;
    public static String field2 = null;
    public static String solrServerUrl = null;
    public static String storageServerUrl = null;

    static {
        try {
            Properties properties = new Properties();
            //String solrConfigPath = MySolrClient.class.getResource("/").getFile()+"solr_config.properties";
            String solrConfigPath = "./solr_config.properties";   // TODO: 16/1/11
            InputStream in = new FileInputStream(solrConfigPath);
            properties.load(in);
            solrServerUrl = properties.getProperty("SolrServerUrl");
            if (solrServerUrl.endsWith("/")){
                solrServerUrl = solrServerUrl.substring(0,solrServerUrl.length()-1);
            }
            storageServerUrl = properties.getProperty("StorageServerUrl");
            String[] fieldNames = properties.getProperty("FieldName").split(",");
            field1 = fieldNames[0];
            field2 = fieldNames[1];
            if (solrServerUrl==null||storageServerUrl==null||field1==null||field2==null){
                throw new NullPointerException("main config 配置项不完整");
            }
        } catch (FileNotFoundException e) {
            System.out.println("The main config file is not exists");
        } catch (IOException e){
            System.out.println("Loading main config file error");
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

    }


}
