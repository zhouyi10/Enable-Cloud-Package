package com.enableets.edu.pakage.framework.ppr.test.service.submit.handler;

import com.enableets.edu.pakage.framework.ppr.test.service.submit.bo.SubmitAttributeBO;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/10
 **/
public interface ISubmitXmlHandler {

    public boolean isSupport(String xml);

    public String submit(String xml, SubmitAttributeBO attribute);
}
