package com.enableets.edu.sdk.ppr;

import com.enableets.edu.sdk.ppr.ppr.parse.xml.IXmlParseHandler;
import com.enableets.edu.sdk.ppr.ppr.parse.xml.XmlParseHandler;
import com.enableets.edu.sdk.ppr.utils.StringUtils;
import com.enableets.edu.sdk.ppr.annotation.NotNull;
import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.configuration.PPRConfigurationInitException;
import com.enableets.edu.sdk.ppr.http.HttpClientFactory;
import com.enableets.edu.sdk.ppr.http.IHttpClient;
import com.enableets.edu.sdk.ppr.ppr.action.IPPRActionHandler;
import com.enableets.edu.sdk.ppr.ppr.action.PPRActionHandler;
import com.enableets.edu.sdk.ppr.ppr.builder.IPPRBuilderHandler;
import com.enableets.edu.sdk.ppr.ppr.builder.PPRBuilderHandler;
import com.enableets.edu.sdk.ppr.ppr.builder.handler.IPaperCardBuilderHandler;
import com.enableets.edu.sdk.ppr.ppr.builder.handler.PaperCardBuilderHandler;
import com.enableets.edu.sdk.ppr.ppr.parse.IPPRParseHandler;
import com.enableets.edu.sdk.ppr.ppr.parse.PPRParseHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *  PPR operation Factory
 *
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public class PprHandlerFactory implements IPprHandlerFactory {

    private final Configuration configuration;  //System config

    private IHttpClient httpClient;

    private IPPRBuilderHandler pprBuilderHandler;  //build ppr

    private IPaperCardBuilderHandler paperCardBuilderHandler;  // bo build xml

    private IPPRParseHandler pprParseHandler;

    private IXmlParseHandler xmlParseHandler;

    private IPPRActionHandler pprActionHandler;  // action handler;

    public PprHandlerFactory(Configuration configuration){
        check(configuration);
        this.configuration = configuration;
        httpClient = HttpClientFactory.getHttpClient(configuration);
        pprBuilderHandler = new PPRBuilderHandler(configuration);
        paperCardBuilderHandler = new PaperCardBuilderHandler();
        pprActionHandler = new PPRActionHandler(configuration);
        xmlParseHandler = new XmlParseHandler();
        pprParseHandler = new PPRParseHandler(configuration);
    }

    @Override
    public IHttpClient getHttpClient(){
        return httpClient;
    }

    @Override
    public IPPRBuilderHandler getPprBuilderHandler() {
        return pprBuilderHandler;
    }

    @Override
    public IPaperCardBuilderHandler getPaperCardBuilderHandler(){
        return paperCardBuilderHandler;
    }

    @Override
    public IPPRParseHandler getPprParseHandler() {
        return pprParseHandler;
    }

    @Override
    public IXmlParseHandler getXmlParseHandler() {
        return xmlParseHandler;
    }

    @Override
    public IPPRActionHandler getPprActionHandler(){
        return pprActionHandler;
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    private static void check(Configuration _conf){
        Field[] declaredFields = _conf.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(NotNull.class)) continue;
            Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
            if (declaredAnnotations == null || declaredAnnotations.length == 0) continue;
            for (Annotation declaredAnnotation : declaredAnnotations) {
                if (!declaredAnnotation.annotationType().equals(NotNull.class)) continue;
                Object attribute = null;
                try {
                    attribute = field.get(_conf);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (attribute == null || StringUtils.isBlank(attribute.toString())){
                    throw new PPRConfigurationInitException("Param \""+ field.getName()+"\" must not be null!");
                }
            }
        }
    }
}
