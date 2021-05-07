/**
 *    Copyright ${license.git.copyrightYears} the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.enableets.edu.pakage.core.core.logging.jdk14;

import com.enableets.edu.pakage.core.core.logging.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @description jdkLog implement
 * @author caleb_liu@enable-ets.com
 * @create 2020/06/22
 **/
public class Jdk14LoggingImpl implements Log {

  //The log class of jdk that truly provides log capabilities
  private final Logger log;

  public Jdk14LoggingImpl(String clazz) {
    log = Logger.getLogger(clazz);
  }

  @Override
  public boolean isDebugEnabled() {
    return log.isLoggable(Level.FINE);
  }

  @Override
  public boolean isInfoEnabled() {
    return log.isLoggable(Level.INFO);
  }

  @Override
  public void error(String s, Throwable e) {
    log.log(Level.SEVERE, s, e);
  }

  @Override
  public void error(String s) {
    log.log(Level.SEVERE, s);
  }

  @Override
  public void debug(String s) {
    log.log(Level.FINE, s);
  }

  @Override
  public void info(String s) {
    log.log(Level.INFO, s);
  }

  @Override
  public void warn(String s) {
    log.log(Level.WARNING, s);
  }

}
