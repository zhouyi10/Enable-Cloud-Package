package com.enableets.edu.pakage.framework.actionflow.service;

import com.enableets.edu.pakage.framework.actionflow.bo.AssessmentActionFlowBO;
import com.enableets.edu.pakage.framework.actionflow.bo.AssessmentMarkStatusBO;
import com.enableets.edu.pakage.framework.actionflow.bo.IdNameMapBO;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private IdentityService identityService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    /**
     * Teacher assigns papers
     *
     * @param assessmentActionFlowBO
     * @return
     */
    public AssessmentActionFlowBO start(AssessmentActionFlowBO assessmentActionFlowBO) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process_assessment");
        Task currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.claim(currentTask.getId(), assessmentActionFlowBO.getTeacherId());

        Map<String, Object> vars = new HashMap<>();
        List<String> recipientList = new ArrayList<>();
        List<IdNameMapBO> studentList = assessmentActionFlowBO.getStudentList();
        studentList.forEach(bo -> {
            recipientList.add(bo.getId());
        });
        vars.put("packageId", assessmentActionFlowBO.getPackageId());
        vars.put("contentId", assessmentActionFlowBO.getContentId());
        vars.put("testId", assessmentActionFlowBO.getTestId());
        vars.put("testName", assessmentActionFlowBO.getTestName());
        vars.put("sender", assessmentActionFlowBO.getTeacherId());
        vars.put("senderName", assessmentActionFlowBO.getTeacherName());
        vars.put("studentList", assessmentActionFlowBO.getStudentList());
        vars.put("recipientList", recipientList);
        taskService.complete(currentTask.getId(), vars);
        assessmentActionFlowBO.setProcessInstanceId(processInstance.getId());
        assessmentActionFlowBO.setCurrentTaskId(currentTask.getId());
        return assessmentActionFlowBO;
    }

    /**
     * List of tasks for students to check their test papers
     *
     * @param userId
     * @return
     */
    public List<AssessmentActionFlowBO> queryTestList(String userId) {
        List<Task> taskList = taskService.createTaskQuery().taskDefinitionKey("answer_3").taskCandidateOrAssigned(userId).orderByTaskCreateTime().desc()
                .list();
        List<AssessmentActionFlowBO> list = getAssessmentActionFlowBOS(taskList);
        return list;
    }

    private String getVariable(Task t, String varName) {
        String s = null;
        Object variable = taskService.getVariable(t.getId(), varName);
        if (variable != null) {
            s = variable.toString();
        }
        return s;
    }

    /**
     * Student Answer Sheet
     *
     * @param processInstanceId
     * @param userId
     * @return
     */
    public AssessmentActionFlowBO submitAnswer(String processInstanceId, String userId) {
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(userId)
                .list();
        AssessmentActionFlowBO bo = new AssessmentActionFlowBO();
        if (list == null || list.isEmpty()) {
            return bo;
        }
        Task task = list.get(0);
        if ("answer_3".equals(task.getTaskDefinitionKey())) {
            taskService.complete(task.getId());
            setVar(processInstanceId, bo);
            bo.setProcessInstanceId(processInstanceId);
            bo.setCurrentTaskId(task.getId());
        }
        return bo;
    }


    /**
     * Teacher Search Test List
     *
     * @param sender
     * @return
     */
    public List<AssessmentMarkStatusBO> queryMarkList(String sender, int offset, int rows) {
        List<Task> taskList = taskService.createTaskQuery().taskDefinitionKey("mark_4").taskCandidateOrAssigned(sender).orderByTaskCreateTime().desc()
                .list();
        List<AssessmentMarkStatusBO> list = new ArrayList<>();
        taskList.forEach((t) -> {
            AssessmentMarkStatusBO bo = new AssessmentMarkStatusBO();
            Date createTime = t.getCreateTime();
            bo.setPackageId(getVariable(t, "packageId"));
            bo.setContentId(getVariable(t, "contentId"));
            bo.setTestId(getVariable(t, "testId"));
            bo.setTestName(getVariable(t, "testName"));
            bo.setTeacherId(getVariable(t, "sender"));
            bo.setTeacherName(getVariable(t, "senderName"));
            bo.setProcessInstanceId(t.getProcessInstanceId());
            bo.setCurrentTaskId(t.getId());
            bo.setCreateTime(createTime);
            bo.setStatus("正在批阅");
            List<IdNameMapBO> studentList = (List<IdNameMapBO>) taskService.getVariable(t.getId(), "studentList");
            bo.setStudentList(studentList);
            list.add(bo);
        });

        List<HistoricTaskInstance> hisTasks = historyService.createHistoricTaskInstanceQuery().taskDefinitionKey("mark_4").taskAssignee(sender).list();
        hisTasks.forEach(his -> {
            if (his.getEndTime() != null) {
                AssessmentMarkStatusBO bo = new AssessmentMarkStatusBO();
                String processInstanceId = his.getProcessInstanceId();
                List<HistoricVariableInstance> hisVars = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
                hisVars.forEach(var -> {
                    if (var.getVariableName().equals("packageId")) {
                        bo.setPackageId(var.getValue().toString());
                    }
                    if (var.getVariableName().equals("contentId")) {
                        bo.setContentId(var.getValue().toString());
                    }
                    if (var.getVariableName().equals("testId")) {
                        bo.setTestId(var.getValue().toString());
                    }
                    if (var.getVariableName().equals("testName")) {
                        bo.setTestName(var.getValue().toString());
                    }
                    if (var.getVariableName().equals("sender")) {
                        bo.setTeacherId(var.getValue().toString());
                    }
                    if (var.getVariableName().equals("senderName")) {
                        bo.setTeacherName(var.getValue().toString());
                    }
                    if (var.getVariableName().equals("studentList")) {
                        List<IdNameMapBO> studentList = (List<IdNameMapBO>) var.getValue();
                        bo.setStudentList(studentList);
                    }
                });
                bo.setCreateTime(his.getCreateTime());
                bo.setProcessInstanceId(processInstanceId);
                bo.setCurrentTaskId(his.getId());
                bo.setStatus("批阅完成");
                list.add(bo);
            }

        });
        list.sort(Comparator.comparing(AssessmentMarkStatusBO::getCreateTime).reversed());
        List<AssessmentMarkStatusBO> collectList = list.stream().skip((offset - 0) * rows).limit(rows).collect(Collectors.toList());
        return collectList;
    }


    /**
     * Teacher marking test papers
     *
     * @param processInstanceId
     * @param senderId
     * @return
     */
    public AssessmentActionFlowBO markAnswer(String processInstanceId, String senderId) {
        AssessmentActionFlowBO bo = new AssessmentActionFlowBO();
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(senderId)
                .list();
        if (list == null || list.isEmpty()) {
            return bo;
        }
        Task task = list.get(0);
        if ("mark_4".equals(task.getTaskDefinitionKey())) {
            AtomicReference<List<String>> recipientList = new AtomicReference<>(new ArrayList<>());
            List<HistoricVariableInstance> hisVars = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
            hisVars.forEach(var -> {
                if (var.getVariableName().equals("packageId")) {
                    bo.setPackageId(var.getValue().toString());
                }
                if (var.getVariableName().equals("contentId")) {
                    bo.setContentId(var.getValue().toString());
                }
                if (var.getVariableName().equals("testId")) {
                    bo.setTestId(var.getValue().toString());
                }
                if (var.getVariableName().equals("testName")) {
                    bo.setTestName(var.getValue().toString());
                }
                if (var.getVariableName().equals("sender")) {
                    bo.setTeacherId(var.getValue().toString());
                }
                if (var.getVariableName().equals("senderName")) {
                    bo.setTeacherName(var.getValue().toString());
                }
                if (var.getVariableName().equals("studentList")) {
                    List<IdNameMapBO> studentList = (List<IdNameMapBO>) var.getValue();
                    bo.setStudentList(studentList);
                }
                if (var.getVariableName().equals("recipientList")) {
                    recipientList.set((List<String>) var.getValue());
                }
            });
            bo.setProcessInstanceId(processInstanceId);
            bo.setCurrentTaskId(task.getId());
            Map<String, Object> vars = new HashMap<>();
            vars.put("recipientList", recipientList.get());
            taskService.complete(task.getId(), vars);
        }
        return bo;
    }

    /**
     * Student View List of test paper results to be viewed
     *
     * @param userId
     * @return
     */
    public List<AssessmentActionFlowBO> queryReportList(String userId) {
        List<Task> taskList = taskService.createTaskQuery().taskDefinitionKey("report_5").taskCandidateOrAssigned(userId)
                .list();
        List<AssessmentActionFlowBO> list = getAssessmentActionFlowBOS(taskList);
        return list;
    }

    private List<AssessmentActionFlowBO> getAssessmentActionFlowBOS(List<Task> taskList) {
        List<AssessmentActionFlowBO> list = new ArrayList<>();
        taskList.forEach((t) -> {
            AssessmentActionFlowBO bo = new AssessmentActionFlowBO();
            bo.setPackageId(getVariable(t, "packageId"));
            bo.setContentId(getVariable(t, "contentId"));
            bo.setTestId(getVariable(t, "testId"));
            bo.setTestName(getVariable(t, "testName"));
            bo.setTeacherId(getVariable(t, "sender"));
            bo.setTeacherName(getVariable(t, "senderName"));
            bo.setProcessInstanceId(t.getProcessInstanceId());
            bo.setCurrentTaskId(t.getId());
            list.add(bo);
        });
        return list;
    }

    /**
     * Students view test papers
     *
     * @param processInstanceId
     * @param userId
     * @return
     */
    public AssessmentActionFlowBO studentReport(String processInstanceId, String userId) {
        AssessmentActionFlowBO bo = new AssessmentActionFlowBO();
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(userId)
                .list();
        if (list == null || list.isEmpty()) {
            return bo;
        }
        Task task = list.get(0);
        if ("report_5".equals(task.getTaskDefinitionKey())) {
            setVar(processInstanceId, bo);
            bo.setProcessInstanceId(processInstanceId);
            bo.setCurrentTaskId(task.getId());
            taskService.complete(task.getId());
        }
        return bo;
    }

    private void setVar(String processInstanceId, AssessmentActionFlowBO bo) {
        List<HistoricVariableInstance> hisVars = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
        hisVars.forEach(var -> {
            if (var.getVariableName().equals("packageId")) {
                bo.setPackageId(var.getValue().toString());
            }
            if (var.getVariableName().equals("contentId")) {
                bo.setContentId(var.getValue().toString());
            }
            if (var.getVariableName().equals("testId")) {
                bo.setTestId(var.getValue().toString());
            }
            if (var.getVariableName().equals("testName")) {
                bo.setTestName(var.getValue().toString());
            }
            if (var.getVariableName().equals("sender")) {
                bo.setTeacherId(var.getValue().toString());
            }
            if (var.getVariableName().equals("senderName")) {
                bo.setTeacherName(var.getValue().toString());
            }
        });
    }


}
