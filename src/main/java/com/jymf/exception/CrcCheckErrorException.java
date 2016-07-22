package com.jymf.exception;

/**
 * Crc校验错误异常
 * @author Zhang
 * @version 0.1
 */

public class CrcCheckErrorException extends Exception{
    public CrcCheckErrorException(String message) {
        super(message);
    }
}
