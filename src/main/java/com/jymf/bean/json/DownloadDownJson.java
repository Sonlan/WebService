package com.jymf.bean.json;

/**
 * 下载下行数据包头中Json POJO类
 * @author Zhang
 * @version 0.1
 */
public class DownloadDownJson {

    private int statusCode = 0;
    private String errorMsg = "";
    private int bodyLength = 0;

    public DownloadDownJson() {
    }

    public DownloadDownJson(int statusCode, String errorMsg, int bodyLength) {
        this.statusCode = statusCode;
        this.errorMsg = errorMsg;
        this.bodyLength = bodyLength;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }
}
