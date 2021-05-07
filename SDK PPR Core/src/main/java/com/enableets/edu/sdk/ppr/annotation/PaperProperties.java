package com.enableets.edu.sdk.ppr.annotation;

import java.lang.annotation.*;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/17
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface PaperProperties {

    String value() default "";

    boolean isNullNotShow() default false;
 }
