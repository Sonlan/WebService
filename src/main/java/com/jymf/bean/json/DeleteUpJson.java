package com.jymf.bean.json;

/**
 * 删除上行数据包头中Json POJO类
 * @author Zhang
 * @version 0.1
 */
public class DeleteUpJson {
    private String username = null;
    private String itemId = null;
    private int fileOrder = 0;

    //用于判断上行数据中是否有缺项,其他上行json对象同名方法功能一样
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
