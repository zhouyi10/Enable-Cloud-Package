package com.enableets.edu.pakage.framework.actionflow.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/15 13:15
 */
public class MyTaskListener implements TaskListener {

    private static final long serialVersionUID = -3971951225455575517L;

    //任务监听
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if ("create".endsWith(eventName)) {
            System.out.println("create=========");
        } else if ("assignment".endsWith(eventName)) {
            System.out.println("assignment========");
        } else if ("complete".endsWith(eventName)) {
            System.out.println("complete===========");
        } else if ("delete".endsWith(eventName)) {
            System.out.println("delete=============");
        }
    }

}
