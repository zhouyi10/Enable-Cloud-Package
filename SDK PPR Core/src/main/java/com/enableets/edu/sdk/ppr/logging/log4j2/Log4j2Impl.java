package com.enableets.edu.sdk.ppr.logging.log4j2;

import com.enableets.edu.sdk.ppr.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.AbstractLogger;

/**
 * @description Log4j2 implement
 * @author caleb_liu@enable-ets.com
 * @create 2020/06/22
 **/

public class Log4j2Impl implements Log {

    private final Log log;

    public Log4j2Impl(String clazz) {
        Logger logger = LogManager.getLogger(clazz);

        if (logger instanceof AbstractLogger) {
            log = new Log4j2AbstractLoggerImpl((AbstractLogger) logger);
        } else {
            log = new Log4j2LoggerImpl(logger);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return this.log.isInfoEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        this.log.error(s, e);
    }

    @Override
    public void error(String s) {
        this.log.error(s);
    }

    @Override
    public void debug(String s) {
        this.log.debug(s);
    }

    @Override
    public void info(String s) {
        this.log.info(s);
    }

    @Override
    public void warn(String s) {
        this.log.warn(s);
    }
}
