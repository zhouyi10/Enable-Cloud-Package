package com.enableets.edu.pakage.core.core.logging;

/**
 * @author caleb_liu@enable-ets.com
 * @description Log factory
 * @create 2020/06/22
 **/

public interface Log {

  boolean  isDebugEnabled();

  boolean isInfoEnabled();

  void debug(String s);

  void info(String s);

  void warn(String s);

  void error(String s, Throwable e);

  void error(String s);
}
