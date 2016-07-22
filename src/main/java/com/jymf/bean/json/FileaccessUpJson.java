package com.jymf.bean.json;

/**
 * 文件获取上行数据包头中Json POJO类
 * @author Zhang
 * @version 0.1
 */
public class FileaccessUpJson {
    private String username = null;
    private String itemId = null;
    private int fileOrder = 0;

    public boolean isComplete(){
        return (username!=null&&itemId!=null&&fileOrder!=0);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getFileOrder() {
        return fileOrder;
    }

    public void setFileOrder(int fileOrder) {
        this.fileOrder = fileOrder;
    }

}
