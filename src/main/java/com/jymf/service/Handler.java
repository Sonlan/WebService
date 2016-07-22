package com.jymf.service;

/**
 * 各类处理器需要实现的抽象接口
 * @author Zhang
 * @version 0.1
 */
public interface Handler {
    public void handle();
    public String getJsonString();
    public byte[] getContent();
}
