package com.enableets.edu.pakage.manager;

import javax.servlet.http.HttpServletRequest;

import com.enableets.edu.sdk.actionflow.annotation.EnableActionFlowServiceSDK;
import com.enableets.edu.sdk.assessment.annotation.EnableAssessmentServiceSDK;
import com.enableets.edu.sdk.catalog.v2.core.annotation.EnableCatalogV2ServiceSDK;
import com.enableets.edu.sdk.cloudclass.annotation.EnableCloudClassServiceSDK;
import com.enableets.edu.sdk.group.annotation.EnableGroupServiceSDK;
import com.enableets.edu.sdk.steptask.annotation.EnableStepV2ServiceSDK;
import com.enableets.edu.sdk.teachingassistant.annotation.EnableTeachingAssistantServiceSDK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import com.enableets.edu.framework.core.bootstrap.ApplicationBootstrap;
import com.enableets.edu.framework.core.util.I18nUtils;
import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.module.service.EnableETSService;
import com.enableets.edu.module.service.core.GlobalServiceExceptionHandler;
import com.enableets.edu.pakage.manager.core.Constants;
import com.enableets.edu.pakage.manager.core.PackageConfigReader;
import com.enableets.edu.pakage.microservice.annotation.EnablePackageMicroService;
import com.enableets.edu.sdk.activity.annotation.EnableActivityV2ServiceSDK;
import com.enableets.edu.sdk.content.annotation.EnableContentServiceSDK;
import com.enableets.edu.sdk.filestorage.annotation.EnableFileStorageServiceSDK;
import com.enableets.edu.sdk.pakage.annotation.EnablePackageServiceSDK;
import com.enableets.edu.sdk.paper.annotation.EnablePaperServiceSDK;
import com.enableets.edu.sdk.school3.annotation.EnableSchoolServiceSDK;
import com.enableets.edu.sdk.school3.v2.annotation.EnableSchoolServiceV2SDK;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

import java.util.Locale;

/**
 * Micro Service Project Startup
 * @author walle_yu@enable-ets.com
 * @since 2020/09/25
 */
@SpringBootApplication(scanBasePackages = {"com.enableets.edu.pakage.manager"},exclude = {DataSourceAutoConfiguration.class})
@Configuration
@EnableJms
@EnableScheduling
@EnableActivityV2ServiceSDK
@EnableSchoolServiceV2SDK
@EnableSchoolServiceSDK
@EnableContentServiceSDK
@EnablePackageServiceSDK
@EnablePaperServiceSDK
@EnableFileStorageServiceSDK
@EnableCatalogV2ServiceSDK
@EnableTeachingAssistantServiceSDK
@EnableETSService
@EnableActionFlowServiceSDK
@EnablePackageMicroService
@EnableGroupServiceSDK
@EnableAssessmentServiceSDK
@EnableCloudClassServiceSDK
@EnableStepV2ServiceSDK
@PropertySource(value = {"classpath:auto-mark-strategy.properties"})
public class PackageManagerBootstrap extends ApplicationBootstrap {

    @Autowired
    private PackageConfigReader packageConfigReader;

    public static void main(String[] args) {
        SpringApplication.run(PackageManagerBootstrap.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(Constants.CONTEXT_PATH + "/custom/package/**") .addResourceLocations("classpath:/static/custom/package/");
        registry.addResourceHandler(Constants.CONTEXT_PATH + "/comm/**").addResourceLocations("classpath:/static/comm/");
    }

    @Override
    @Bean
    public LocaleResolver localeResolver() {
        Locale locale = I18nUtils.localeFromString(this.locale, (Locale)null);
        LocaleResolver localeResolver = new FixedLocaleResolver(locale);
        return localeResolver;
    }

    @Bean
    public com.enableets.edu.pakage.core.core.Configuration configuration(){
        return new com.enableets.edu.pakage.core.core.Configuration().setClientId(packageConfigReader.getClientId()).setClientSecret(packageConfigReader.getClientSecret()).setPPRTempPath(packageConfigReader.getPprMakeTempDir())
                .setServerAddress(packageConfigReader.getNginxServer());
    }

    @Configuration
    @ControllerAdvice("com.enableets.edu.pakage")
    public static class CustomGlobalServiceExceptionHandler extends GlobalServiceExceptionHandler {

    }

    @ControllerAdvice(basePackages = {"com.enableets.edu.pakage.manager"})
    public static class GlobalVariableController{

        @ModelAttribute
        public void get(Model model){
            Locale _locale = ((LocaleResolver) SpringBeanUtils.getBean(LocaleResolver.class)).resolveLocale((HttpServletRequest)null);
            model.addAttribute("_locale", _locale.toString());
            DynamicStringProperty stringProperty = DynamicPropertyFactory.getInstance().getStringProperty("build.version", "0002");
            model.addAttribute("_v", stringProperty.get());
            model.addAttribute("_contextPath", Constants.CONTEXT_PATH);
        }
    }

}