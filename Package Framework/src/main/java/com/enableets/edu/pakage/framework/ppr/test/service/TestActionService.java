package com.enableets.edu.pakage.framework.ppr.test.service;

import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.enableets.edu.pakage.framework.ppr.core.TestActionDescriptionConstants;
import com.enableets.edu.pakage.framework.ppr.test.bo.TestActionFlowBO;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/12
 **/
//@Service
public class TestActionService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Autowired
    private TestInfoService testInfoService;

    public String publish(TestActionFlowBO testActionFlowBO) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("sender", testActionFlowBO.getSender());
        vars.put("testId", testActionFlowBO.getTestId());
        vars.put("recipientList", testActionFlowBO.getRecipientList());
        vars.put("startTime", testActionFlowBO.getStartTime());
        vars.put("endTime", testActionFlowBO.getEndTime());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(TestActionDescriptionConstants.TEST_ACTION_PROCESS_KEY, vars);
        Task currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.claim(currentTask.getId(), testActionFlowBO.getSender());
        taskService.complete(currentTask.getId(), vars);
        return processInstance.getId();
    }

    public boolean testStart(String testId, String userId) {
        String processInstanceId = this.getProcessInstanceId(testId);
        if (this.isTestComplete(testId)) return false;
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(TestActionDescriptionConstants.TEST_ACTION_ANSWER_TASK_KEY).taskAssignee(userId).singleResult();
        if (task != null) return true;
        HistoricTaskInstance hisTask = historyService.createHistoricTaskInstanceQuery().processDefinitionId(processInstanceId).taskAssignee(userId).taskDefinitionKey(TestActionDescriptionConstants.TEST_ACTION_ANSWER_TASK_KEY).singleResult();
        if (hisTask != null) return true;
        return false;
    }

    public String answer(String testId, String userId, String testUserId){
        if (isMarked(userId, testId)) return "";
        return this.completePersonalTask(testId, userId, testUserId, TestActionDescriptionConstants.TEST_ACTION_ANSWER_TASK_KEY);
    }

    public boolean isMarked(String userId, String testId) {
        String processInstanceId = this.getProcessInstanceId(testId);
        if (this.isTestComplete(testId)) return true;
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).taskAssignee(userId).taskDefinitionKey(TestActionDescriptionConstants.TEST_ACTION_MARK_TASK_KEY).finished().singleResult();
        if (task != null) return true;
        return false;
    }

    public boolean isTestComplete(String testId) {
        String processInstanceId = this.getProcessInstanceId(testId);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance == null) return true;
        return false;
    }

    public String mark(String testId, String userId, String testUserId){
        return this.completePersonalTask(testId, userId, testUserId, TestActionDescriptionConstants.TEST_ACTION_MARK_TASK_KEY);
    }

    private String getProcessInstanceId(String testId){
        return testInfoService.get(testId).getProcessInstanceId();
    }

    private String completePersonalTask(String testId, String userId, String testUserId, String taskKey) {
        String processInstanceId = this.getProcessInstanceId(testId);
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(userId).taskDefinitionKey(taskKey).singleResult();
        if (task != null) {
            Map<String, Object> vars = new HashMap<>();
            vars.put("userId", userId);
            vars.put("testUserId", testUserId);
            vars.put("testId", testId);
            taskService.complete(task.getId(), vars);
            return task.getId();
        }
        return null;

    }

    private Map<String, Object> getProcessInstanceVars(String testId){
        String processInstanceId = this.getProcessInstanceId(testId);
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
        Map<String, Object> vars = new HashMap<>();
        list.forEach(e -> {
            if (e.getVariableName().equals("userId")) vars.put("sender", e.getValue().toString());
            if (e.getVariableName().equals("testId")) vars.put("testId", e.getValue().toString());
            if (e.getVariableName().equals("recipientList")) vars.put("recipientList", (List<String>)e.getValue());
            if (e.getVariableName().equals("startTime")) vars.put("startTime", (Date)e.getValue());
            if (e.getVariableName().equals("endTime")) vars.put("endTime", (Date)e.getValue());
        });
        return vars;
    }

    public static String getISO8601Timestamp(Date date){
        if (date == null) return null;
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(date);
        return nowAsISO;
    }

    public static Date getNextMin() {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.MINUTE, minute + 1);
        Date date = calendar.getTime();
        return date;
    }
}
