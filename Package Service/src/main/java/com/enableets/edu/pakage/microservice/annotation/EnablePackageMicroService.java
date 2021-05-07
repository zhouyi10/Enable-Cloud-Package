package com.enableets.edu.pakage.microservice.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 **/
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({PackageMicroServiceConfiguration.class})
public @interface EnablePackageMicroService {
}
