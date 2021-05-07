package com.enableets.edu.pakage.framework.workflow.service;

import com.enableets.edu.pakage.framework.workflow.ActivitiUtil;
import com.enableets.edu.pakage.framework.workflow.po.VacTask;
import com.enableets.edu.pakage.framework.workflow.po.VacationPO;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author tony_liu@enable-ets.com
 * @since 2021/2/26
 */
@Service
public class VacationService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private IdentityService identityService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;


    private static final String PROCESS_DEFINE_KEY = "vacationProcess";


    public void startVac(String userId, String days, String reason) {

        identityService.setAuthenticatedUserId(userId);
        // 开始流程
        ProcessInstance vacationInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINE_KEY);
        // 查询当前任务
        Task currentTask = taskService.createTaskQuery().processInstanceId(vacationInstance.getId()).singleResult();
        // 申明任务
        taskService.claim(currentTask.getId(), userId);

        Map<String, Object> vars = new HashMap<>();
        vars.put("applyUser", userId);
        vars.put("days", days);
        vars.put("reason", reason);

        taskService.complete(currentTask.getId(), vars);

    }

    public List<VacationPO> getVacByUserId(String userId) {
        List<ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery().startedBy(userId).list();
        List<VacationPO> vacList = new ArrayList<>();
        for (ProcessInstance instance : instanceList) {
            VacationPO vac = getVac(instance);
            vacList.add(vac);
        }
        return vacList;
    }

    private VacationPO getVac(ProcessInstance instance) {
        Integer days = runtimeService.getVariable(instance.getId(), "days", Integer.class);
        String reason = runtimeService.getVariable(instance.getId(), "reason", String.class);
        VacationPO vac = new VacationPO();
        vac.setApplyUser(instance.getStartUserId());
        vac.setDays(days);
        vac.setReason(reason);
        Date startTime = instance.getStartTime(); // activiti 6 才有
        vac.setApplyTime(startTime);
        vac.setApplyStatus(instance.isEnded() ? "申请结束" : "等待审批");
        return vac;
    }


    public List<VacTask> myAudit(String userId) {
        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser(userId)
                .orderByTaskCreateTime().desc().list();
        List<VacTask> vacTaskList = new ArrayList<>();
        for (Task task : taskList) {
            VacTask vacTask = new VacTask();
            vacTask.setId(task.getId());
            vacTask.setName(task.getName());
            vacTask.setCreateTime(task.getCreateTime());
            String instanceId = task.getProcessInstanceId();
            ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            VacationPO vac = getVac(instance);
            vacTask.setVac(vac);
            vacTaskList.add(vacTask);
        }
        return vacTaskList;
    }

    public void passAudit(String userId, String taskId, String auditResult) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("result", auditResult);
        vars.put("auditor", userId);
        vars.put("auditTime", new Date());
        taskService.claim(taskId, userId);
        taskService.complete(taskId, vars);
    }

    public List<VacationPO> myVacRecord(String userId) {
        List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(PROCESS_DEFINE_KEY).startedBy(userId).finished()
                .orderByProcessInstanceEndTime().desc().list();

        List<VacationPO> vacList = new ArrayList<>();
        for (HistoricProcessInstance hisInstance : hisProInstance) {
            VacationPO vacation = new VacationPO();
            vacation.setApplyUser(hisInstance.getStartUserId());
            vacation.setApplyTime(hisInstance.getStartTime());
            vacation.setApplyStatus("申请结束");
            List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(hisInstance.getId()).list();
            ActivitiUtil.setVars(vacation, varInstanceList);
            vacList.add(vacation);
        }
        return vacList;
    }


    public List<VacationPO> myAuditRecord(String userId) {
        List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(PROCESS_DEFINE_KEY).involvedUser(userId).finished()
                .orderByProcessInstanceEndTime().desc().list();

        List<String> auditTaskNameList = new ArrayList<>();
        auditTaskNameList.add("经理审批");
        auditTaskNameList.add("总监审批");
        List<VacationPO> vacList = new ArrayList<>();
        for (HistoricProcessInstance hisInstance : hisProInstance) {
            List<HistoricTaskInstance> hisTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(hisInstance.getId()).processFinished()
                    .taskAssignee(userId)
                    .taskNameIn(auditTaskNameList)
                    .orderByHistoricTaskInstanceEndTime().desc().list();
            boolean isMyAudit = false;
            for (HistoricTaskInstance taskInstance : hisTaskInstanceList) {
                if (taskInstance.getAssignee().equals(userId)) {
                    isMyAudit = true;
                }
            }
            if (!isMyAudit) {
                continue;
            }
            VacationPO vacation = new VacationPO();
            vacation.setApplyUser(hisInstance.getStartUserId());
            vacation.setApplyStatus("申请结束");
            vacation.setApplyTime(hisInstance.getStartTime());
            List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(hisInstance.getId()).list();
            ActivitiUtil.setVars(vacation, varInstanceList);
            vacList.add(vacation);
        }
        return vacList;
    }
}
