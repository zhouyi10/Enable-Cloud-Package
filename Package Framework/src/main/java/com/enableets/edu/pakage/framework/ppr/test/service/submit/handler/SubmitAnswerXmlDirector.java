package com.enableets.edu.pakage.framework.ppr.test.service.submit.handler;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/10
 **/
public class SubmitAnswerXmlDirector {

    private SubmitAnswerXmlDirector(){}

    private static List<ISubmitXmlHandler> handlers = new ArrayList<>();

    public static ISubmitXmlHandler getHandler(String xml){
        init();
        ISubmitXmlHandler retHandler = null;
        for (ISubmitXmlHandler handler : handlers) {
            if (handler.isSupport(xml)) {
                retHandler = handler; break;
            }
        }
        return retHandler;
    }

    private static void init(){
        if (CollectionUtils.isEmpty(handlers)){
            handlers.add(new PPRPaperCardXmlHandler());
        }
    }

}
