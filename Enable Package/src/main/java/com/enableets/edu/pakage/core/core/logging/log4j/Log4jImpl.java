package com.enableets.edu.pakage.core.core.logging.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.enableets.edu.pakage.core.core.logging.Log;

/**
 * @description Log4j implement
 * @author caleb_liu@enable-ets.com
 * @create 2020/06/22
 **/

public class Log4jImpl implements Log {

    private static final String FQCN = Log4jImpl.class.getName();

    private final Logger log;

    public Log4jImpl(String clazz) {
        this.log = Logger.getLogger(clazz);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        log.log(FQCN, Level.ERROR, s, e);
    }

    @Override
    public void error(String s) {
        log.log(FQCN, Level.ERROR, s, null);
    }

    @Override
    public void debug(String s) {
        log.log(FQCN, Level.DEBUG, s, null);
    }

    @Override
    public void info(String s) {
        log.log(FQCN, Level.INFO, s, null);
    }

    @Override
    public void warn(String s) {
        log.log(FQCN, Level.WARN, s, null);
    }
}
