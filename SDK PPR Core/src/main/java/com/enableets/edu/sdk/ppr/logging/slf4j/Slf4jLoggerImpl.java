package com.enableets.edu.sdk.ppr.logging.slf4j;

import com.enableets.edu.sdk.ppr.logging.Log;
import org.slf4j.Logger;

/**
 * @author Eduardo Macarron
 */
class Slf4jLoggerImpl implements Log {

  private final Logger log;

  public Slf4jLoggerImpl(Logger logger) {
    log = logger;
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
    log.error(s, e);
  }

  @Override
  public void error(String s) {
    log.error(s);
  }

  @Override
  public void debug(String s) {
    log.debug(s);
  }

  @Override
  public void info(String s) {
    log.info(s);
  }

  @Override
  public void warn(String s) {
    log.warn(s);
  }

}
