package com.jymf.bean.json;

/**
 * 上传下行数据包头中Json POJO类
 * @author Zhang
 * @version 0.1
 */
public class UploadDownJson {

    private int statusCode = 0;
    private String errorMsg = "";

    public UploadDownJson() {
    }

    public UploadDownJson(int statusCode, String errorMsg) {
        this.statusCode = statusCode;
        this.errorMsg = errorMsg;
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
}
