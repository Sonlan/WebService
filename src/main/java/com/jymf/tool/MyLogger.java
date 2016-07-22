package com.jymf.tool;

import org.apache.log4j.Logger;

/**
 * 日志代理类,封装了log4j中的logger,主要工作时在记录日之前做级别判断,减少系统开销
 * @author Zhang
 * @version 0.1
 */
public class MyLogger {

    private Logger logger = null;

    public MyLogger(Logger logger) {
        this.logger = logger;
    }

    public void debug(String msg){
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    public void info(String msg){
        if (logger.isInfoEnabled()) {
            logger.info(msg);
        }
    }

    public void warn(String msg){
        logger.warn(msg);
    }
}
