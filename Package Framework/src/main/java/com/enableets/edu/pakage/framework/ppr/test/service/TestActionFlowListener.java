package com.enableets.edu.pakage.framework.ppr.test.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/15
 **/
public class TestActionFlowListener implements ExecutionListener {

    private static final String END_EVENT_NAME = "end";

    @Override
    public void notify(DelegateExecution delegateExecution) {
        if (delegateExecution.getEventName().equals(END_EVENT_NAME)) {
            System.out.println("考试已经完成！");
        }
    }
}
