package com.enableets.edu.sdk.ppr.logging.slf4j;

import com.enableets.edu.sdk.ppr.logging.Log;
import com.enableets.edu.sdk.ppr.logging.LogFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.spi.LocationAwareLogger;

/**
 * @author caleb_liu@enable-ets.com
 * @description Slf4jLocationAwareLogger implement
 * @create 2020/06/22
 **/
class Slf4jLocationAwareLoggerImpl implements Log {

    private static final Marker MARKER = MarkerFactory.getMarker(LogFactory.MARKER);

    private static final String FQCN = Slf4jImpl.class.getName();

    private final LocationAwareLogger logger;

    Slf4jLocationAwareLoggerImpl(LocationAwareLogger logger) {
        this.logger = logger;
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        logger.log(MARKER, FQCN, LocationAwareLogger.ERROR_INT, s, null, e);
    }

    @Override
    public void error(String s) {
        logger.log(MARKER, FQCN, LocationAwareLogger.ERROR_INT, s, null, null);
    }

    @Override
    public void debug(String s) {
        logger.log(MARKER, FQCN, LocationAwareLogger.DEBUG_INT, s, null, null);
    }

    @Override
    public void info(String s) {
        logger.log(MARKER, FQCN, LocationAwareLogger.INFO_INT, s, null, null);
    }

    @Override
    public void warn(String s) {
        logger.log(MARKER, FQCN, LocationAwareLogger.WARN_INT, s, null, null);
    }

}
