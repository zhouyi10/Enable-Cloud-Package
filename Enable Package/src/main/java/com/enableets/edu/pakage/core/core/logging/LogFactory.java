package com.enableets.edu.pakage.core.core.logging;

import com.enableets.edu.pakage.core.core.logging.blankj.BlankjImpl;
import com.enableets.edu.pakage.core.core.logging.commons.JakartaCommonsLoggingImpl;
import com.enableets.edu.pakage.core.core.logging.jdk14.Jdk14LoggingImpl;
import com.enableets.edu.pakage.core.core.logging.log4j.Log4jImpl;
import com.enableets.edu.pakage.core.core.logging.log4j2.Log4j2Impl;
import com.enableets.edu.pakage.core.core.logging.nologging.NoLoggingImpl;
import com.enableets.edu.pakage.core.core.logging.slf4j.Slf4jImpl;
import com.enableets.edu.pakage.core.core.logging.stdout.StdOutImpl;
import com.enableets.edu.pakage.core.exception.PackageException;

import java.lang.reflect.Constructor;

/**
 * @author caleb_liu@enable-ets.com
 * @description Log factory
 * @create 2020/06/22
 **/

public final class LogFactory {

    /**
     * Marker to be used by logging implementations that support markers
     */
    public static final String MARKER = "PPRLogging";

    private static Constructor<? extends Log> logConstructor;

    public LogFactory() {

    }

    static {
        tryImplementation(LogFactory::useBlankjLogging);
        tryImplementation(LogFactory::useLog4J2Logging);
        tryImplementation(LogFactory::useLog4JLogging);
        tryImplementation(LogFactory::useSlf4jLogging);
        tryImplementation(LogFactory::useCommonsLogging);
        tryImplementation(LogFactory::useJdkLogging);
        tryImplementation(LogFactory::useNoLogging);
    }

    public static Log getLog(Class<?> aClass) {
        return getLog(aClass.getName());
    }

    public static Log getLog(String logger) {
        try {
            return logConstructor.newInstance(logger);
        } catch (Throwable t) {
            throw new PackageException("Error creating logger for logger " + logger + ".  Cause: " + t, t);
        }
    }

    public static synchronized void useCustomLogging(Class<? extends Log> clazz) {
        setImplementation(clazz);
    }

    public static synchronized void useSlf4jLogging() {
        setImplementation(Slf4jImpl.class);
    }

    public static synchronized void useCommonsLogging() {
        setImplementation(JakartaCommonsLoggingImpl.class);
    }

    public static synchronized void useLog4JLogging() {
        setImplementation(Log4jImpl.class);
    }

    public static synchronized void useLog4J2Logging() {
        setImplementation(Log4j2Impl.class);
    }

    public static synchronized void useJdkLogging() {
        setImplementation(Jdk14LoggingImpl.class);
    }

    public static synchronized void useStdOutLogging() {
        setImplementation(StdOutImpl.class);
    }

    public static synchronized void useBlankjLogging() {
        setImplementation(BlankjImpl.class);
    }

    public static synchronized void useNoLogging() {
        setImplementation(NoLoggingImpl.class);
    }

    private static void tryImplementation(Runnable runnable) {
        if (logConstructor == null) {
            try {
                runnable.run();
            } catch (Throwable t) {

            }
        }
    }

    public static void setImplementation(Class<? extends Log> implClass) {
        try {
            Constructor<? extends Log> candidate = implClass.getConstructor(String.class);
            Log log = candidate.newInstance(LogFactory.class.getName());
            if (log.isDebugEnabled()) {
                log.debug("Logging initialized using '" + implClass + "' adapter.");
            }
            logConstructor = candidate;
        } catch (Throwable t) {
            throw new PackageException("Error setting Log implementation.  Cause: " + t, t);
        }
    }
}
