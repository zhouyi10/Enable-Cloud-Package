package com.enableets.edu.pakage.core.core.logging.stdout;

import com.enableets.edu.pakage.core.core.logging.Log;


public class StdOutImpl implements Log {

  public StdOutImpl(String clazz) {
    // Do Nothing
  }

  @Override
  public boolean isDebugEnabled() {
    return true;
  }

  @Override
  public boolean isInfoEnabled() {
    return true;
  }

  @Override
  public void error(String s, Throwable e) {
    System.err.println(s);
    e.printStackTrace(System.err);
  }

  @Override
  public void error(String s) {
    System.err.println(s);
  }

  @Override
  public void debug(String s) {
    System.out.println(s);
  }

  @Override
  public void info(String s) {
    System.out.println(s);
  }

  @Override
  public void warn(String s) {
    System.out.println(s);
  }
}
