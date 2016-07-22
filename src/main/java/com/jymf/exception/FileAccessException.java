package com.jymf.exception;

/**
 * 从StorageServer走Http协议获取文件异常
 * @author Zhang
 * @version 0.1
 */
public class FileAccessException extends Exception {
    public FileAccessException(String message) {
        super(message);
    }
}
