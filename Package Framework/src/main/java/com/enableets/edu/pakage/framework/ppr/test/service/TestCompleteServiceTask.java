package com.enableets.edu.pakage.framework.ppr.test.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/14
 **/
public class TestCompleteServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Test Complete!");
    }
}
