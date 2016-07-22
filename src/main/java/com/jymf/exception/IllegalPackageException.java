package com.jymf.exception;

/**
 * 数据包不合法异常
 * @author Zhang
 * @version 0.1
 */
public class IllegalPackageException extends Exception{
    public IllegalPackageException(String message) {
        super(message);
    }
}
