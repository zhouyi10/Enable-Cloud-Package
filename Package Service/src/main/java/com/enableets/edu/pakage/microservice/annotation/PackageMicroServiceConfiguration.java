package com.enableets.edu.pakage.microservice.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.enableets.edu.framework.core.util.token.CustomTokenGenerator;
import com.enableets.edu.framework.core.util.token.ITokenGenerator;
import com.enableets.edu.framework.core.util.token.RandomTokenGenerator;
import com.enableets.edu.module.service.core.DefaultSwaggerConfiguration;
import com.enableets.edu.module.service.core.GlobalServiceExceptionHandler;
import com.enableets.edu.module.service.core.ValidateVOAspect;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 **/
@ComponentScan(basePackages = {"com.enableets.edu.pakage.framework", "com.enableets.edu.pakage.microservice", "com.enableets.edu.ppr.adapter.service"}, nameGenerator = PackageMicroServiceConfiguration.CustomAnnotationBeanNameGenerator.class)
@MapperScan(basePackages = {"com.enableets.edu.pakage.framework.ppr.assessment.dao", "com.enableets.edu.pakage.framework.ppr.paper.dao", "com.enableets.edu.pakage.framework.ppr.question.dao", "com.enableets.edu.ppr.adapter.dao"})
public class PackageMicroServiceConfiguration extends DefaultSwaggerConfiguration {

    @Value("${system-identifier.datacenter-id}")
    private Long datacenterId;

    @Value("${system-identifier.worker-id}")
    private Long workerId;

    // swagger - restful package
    public static final String RESTFUL_BASE_PACKAGE = "com.enableets.edu.pakage.microservice";

    // swagger - doc title
    private static final String TITLE = "Package Service RESTFUL APIs";

    // swagger - description
    private static final String DESCRIPTION = "diagnostic information microservice RESTful API document description";

    // swagger - doc version
    private static final String VERSION = "1.0.0";

    @Override
    protected ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(TITLE).description(DESCRIPTION).version(VERSION).build();
    }

    @Override
    protected String getRestfulControllerBasePackage() {
        return RESTFUL_BASE_PACKAGE;
    }

    @Bean
    public ITokenGenerator tokenGenerator() {
        return new CustomTokenGenerator(workerId, datacenterId);
    }

    public static class CustomAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator {

        @Override
        protected String buildDefaultBeanName(BeanDefinition definition) {
            return "package_" + super.buildDefaultBeanName(definition);
        }
    }

    @Configuration
    @ControllerAdvice(RESTFUL_BASE_PACKAGE)
    public static class CustomGlobalServiceExceptionHandler extends GlobalServiceExceptionHandler {

    }

    @Aspect
    @Configuration
    public static class CustomValidateVOAspect extends ValidateVOAspect {

        @Override
        @Pointcut("execution(* com.enableets.edu.pakage.microservice..restful.*.*(..))")
        public void aspect() {

        }
    }
}


