package com.jymf.bean.json;

import java.util.TreeMap;

/**
 * 下载下行数据包包体中Json POJO类
 * @author Zhang
 * @version 0.1
 */
public class RespContentJson {
    private String itemId = null;
    private TreeMap<Integer, String> urls = new TreeMap<Integer, String>();

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public TreeMap<Integer, String> getUrls() {
        return urls;
    }

    public void setUrls(TreeMap<Integer, String> urls) {
        this.urls = urls;
    }

    public void setOneUrl(Integer order, String url){
        urls.put(order,url);
    }

}
