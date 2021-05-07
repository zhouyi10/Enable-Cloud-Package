package com.enableets.edu.pakage.framework.actionflow.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/15 13:15
 */
public class MyExecutionListener implements ExecutionListener ,TaskListener{

    private static final long serialVersionUID = 5909307503756662898L;

    //流程监听
    @Override
    public void notify(DelegateExecution delegateExecution) {

        String eventName = delegateExecution.getEventName();
        if ("start".equals(eventName)) {
            System.out.println("start=========");
        } else if ("end".equals(eventName)) {
            System.out.println("end=========");
        } else if ("take".equals(eventName)) {
            System.out.println("take=========");

        }
    }

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
