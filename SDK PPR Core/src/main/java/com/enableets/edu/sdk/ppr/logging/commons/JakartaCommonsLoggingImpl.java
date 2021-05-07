package com.enableets.edu.sdk.ppr.logging.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @description JakartaCommonsLogging implement
 * @author caleb_liu@enable-ets.com
 * @create 2020/06/22
 **/
public class JakartaCommonsLoggingImpl implements com.enableets.edu.sdk.ppr.logging.Log {

  private final Log log;

  public JakartaCommonsLoggingImpl(String clazz) {
    log = LogFactory.getLog(clazz);
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
