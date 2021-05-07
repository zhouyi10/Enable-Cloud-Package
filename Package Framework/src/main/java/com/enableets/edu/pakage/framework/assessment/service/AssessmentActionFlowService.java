package com.enableets.edu.pakage.framework.assessment.service;

import com.enableets.edu.pakage.framework.assessment.bo.AssessmentActionFlowBO;
import com.enableets.edu.pakage.framework.assessment.bo.IdNameMapBO;
import com.enableets.edu.sdk.actionflow.dto.ActionFlowCompleteInfoDTO;
import com.enableets.edu.sdk.actionflow.dto.ActionFlowStartInfoDTO;
import com.enableets.edu.sdk.actionflow.dto.ActionFlowTaskInfoDTO;
import com.enableets.edu.sdk.actionflow.service.IActionFlowBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/23 16:49
 */
@Service
public class AssessmentActionFlowService {


    @Autowired
    private IActionFlowBaseService actionFlowBaseService;

    /**
     * assessment publish
     *
     * @param assessmentActionFlowBO
     * @return
     */
    public AssessmentActionFlowBO publish(AssessmentActionFlowBO assessmentActionFlowBO) {
        ActionFlowStartInfoDTO dto = new ActionFlowStartInfoDTO();
        dto.setBusinessKey("process_assessment");
        dto.setUserId(assessmentActionFlowBO.getTeacherId());
        Map<String, Object> vars = new HashMap<String, Object>();
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
        dto.setVariables(vars);
        String pId = actionFlowBaseService.satrt(dto);
        assessmentActionFlowBO.setProcessInstanceId(pId);
        return assessmentActionFlowBO;
    }

    /**
     * query assessment publish
     *
     * @param teacherId
     * @return
     */
    public List<AssessmentActionFlowBO> queryPublishList(String teacherId) {
        List<ActionFlowTaskInfoDTO> publishList = actionFlowBaseService.queryTasks(null, teacherId, "send_2");
        List<AssessmentActionFlowBO> list = new ArrayList<>();
        publishList.forEach(p -> {
            AssessmentActionFlowBO bo = new AssessmentActionFlowBO();
            Map<String, Object> variables = p.getVariables();
            bo.setProcessInstanceId(p.getBusinessId());
            bo.setCurrentTaskId(p.getStepId());
            bo.setPackageId(getValue(variables, "packageId"));
            bo.setContentId(getValue(variables, "contentId"));
            bo.setTestId(getValue(variables, "testId"));
            bo.setTestName(getValue(variables, "testName"));
            bo.setTeacherId(getValue(variables, "sender"));
            bo.setTeacherName(getValue(variables, "senderName"));
            Object studentList = variables.get("studentList");
            List<IdNameMapBO> mapBOS = castList(studentList);
            bo.setStudentList(mapBOS);
            list.add(bo);
        });
        return list;
    }

    private String getValue(Map<String, Object> variables, String key) {
        if (variables.get(key) == null) {
            return null;
        }
        return variables.get(key).toString();
    }

    /**
     * query test
     *
     * @param studentId
     * @return
     */
    public List<AssessmentActionFlowBO> queryTestList(String studentId) {
        List<ActionFlowTaskInfoDTO> testList = actionFlowBaseService.queryTasks(null, studentId, "answer_3");
        List<AssessmentActionFlowBO> list = new ArrayList<>();
        testList.forEach(p -> {
//            if (p.getIsComplete() == false) {
                AssessmentActionFlowBO bo = new AssessmentActionFlowBO();
                Map<String, Object> variables = p.getVariables();
                bo.setProcessInstanceId(p.getBusinessId());
                bo.setCurrentTaskId(p.getStepId());
                bo.setPackageId(getValue(variables, "packageId"));
                bo.setContentId(getValue(variables, "contentId"));
                bo.setTestId(getValue(variables, "testId"));
                bo.setTestName(getValue(variables, "testName"));
                bo.setTeacherId(getValue(variables, "sender"));
                bo.setTeacherName(getValue(variables, "senderName"));
                list.add(bo);
//            }
        });
        return list;
    }

    /**
     * test submit
     *
     * @param processInstanceId
     * @param studentId
     * @return
     */
    public AssessmentActionFlowBO submitAnswer(String processInstanceId, String studentId) {
        ActionFlowCompleteInfoDTO infoDTO = new ActionFlowCompleteInfoDTO();
        infoDTO.setBusinessId(processInstanceId);
        infoDTO.setStepKey("answer_3");
        infoDTO.setUserId(studentId);
        ActionFlowTaskInfoDTO dto = actionFlowBaseService.complete(infoDTO);
        AssessmentActionFlowBO bo = new AssessmentActionFlowBO();
        Map<String, Object> variables = dto.getVariables();
        bo.setProcessInstanceId(dto.getBusinessId());
        bo.setCurrentTaskId(dto.getStepId());
        if (variables != null) {
            bo.setPackageId(getValue(variables, "packageId"));
            bo.setContentId(getValue(variables, "contentId"));
            bo.setTestId(getValue(variables, "testId"));
            bo.setTestName(getValue(variables, "testName"));
            bo.setTeacherId(getValue(variables, "sender"));
            bo.setTeacherName(getValue(variables, "senderName"));
        }
        bo.setIsComplete(dto.getStatus());
        bo.setCreateTime(dto.getCreateTime());
        return bo;
    }

    /**
     * query mark
     *
     * @param teacherId
     * @param offset
     * @param rows
     * @return
     */
    public List<AssessmentActionFlowBO> queryMarkList(String teacherId, int offset, int rows) {
        List<ActionFlowTaskInfoDTO> publishList = actionFlowBaseService.queryTasks(null, teacherId, "mark_4");
        List<AssessmentActionFlowBO> list = new ArrayList<>();
        publishList.forEach(p -> {
            AssessmentActionFlowBO bo = new AssessmentActionFlowBO();
            Map<String, Object> variables = p.getVariables();
            bo.setProcessInstanceId(p.getBusinessId());
            bo.setCurrentTaskId(p.getStepId());
            bo.setPackageId(getValue(variables, "packageId"));
            bo.setContentId(getValue(variables, "contentId"));
            bo.setTestId(getValue(variables, "testId"));
            bo.setTestName(getValue(variables, "testName"));
            bo.setTeacherId(getValue(variables, "sender"));
            bo.setTeacherName(getValue(variables, "senderName"));
            Object studentList = variables.get("studentList");
            List<IdNameMapBO> mapBOS = castList(studentList);
            bo.setStudentList(mapBOS);
            bo.setIsComplete(p.getStatus());
            bo.setCreateTime(p.getCreateTime());
            list.add(bo);
        });
        list.sort(Comparator.comparing(AssessmentActionFlowBO::getCreateTime).reversed());
        List<AssessmentActionFlowBO> collectList = list.stream().skip((offset - 0) * rows).limit(rows).collect(Collectors.toList());
        return collectList;
    }


    public List<IdNameMapBO> castList(Object obj) {
        List<IdNameMapBO> result = new ArrayList<IdNameMapBO>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                IdNameMapBO idNameMapBO = new IdNameMapBO();
                String s = o.toString();
                String substring = s.substring(0, s.length() - 1);
                String[] split = substring.split(",");
                String id = split[0].split("=")[1];
                String name = split[1].split("=")[1];
                idNameMapBO.setId(id);
                idNameMapBO.setName(name);
                result.add(idNameMapBO);
            }
            return result;
        }
        return null;
    }

    /**
     * mark
     *
     * @param processInstanceId
     * @param teacherId
     * @return
     */
    public AssessmentActionFlowBO markAnswer(String processInstanceId, String teacherId) {
        ActionFlowCompleteInfoDTO infoDTO = new ActionFlowCompleteInfoDTO();
        infoDTO.setBusinessId(processInstanceId);
        infoDTO.setStepKey("mark_4");
        infoDTO.setUserId(teacherId);
        ActionFlowTaskInfoDTO dto = actionFlowBaseService.complete(infoDTO);
        AssessmentActionFlowBO bo = new AssessmentActionFlowBO();
        Map<String, Object> variables = dto.getVariables();
        bo.setProcessInstanceId(dto.getBusinessId());
        bo.setCurrentTaskId(dto.getStepId());
        bo.setPackageId(getValue(variables, "packageId"));
        bo.setContentId(getValue(variables, "contentId"));
        bo.setTestId(getValue(variables, "testId"));
        bo.setTestName(getValue(variables, "testName"));
        bo.setTeacherId(getValue(variables, "sender"));
        bo.setTeacherName(getValue(variables, "senderName"));
        Object studentList = variables.get("studentList");
        List<IdNameMapBO> mapBOS = castList(studentList);
        bo.setStudentList(mapBOS);
        bo.setIsComplete(dto.getStatus());
        bo.setCreateTime(dto.getCreateTime());
        return bo;
    }

    /**
     * query report
     *
     * @param studentId
     * @return
     */
    public List<AssessmentActionFlowBO> queryReportList(String studentId) {
        List<ActionFlowTaskInfoDTO> testList = actionFlowBaseService.queryTasks(null, studentId, "report_5");
        List<AssessmentActionFlowBO> list = new ArrayList<>();
        testList.forEach(p -> {
            if (p.getStatus() == false) {
                AssessmentActionFlowBO bo = new AssessmentActionFlowBO();
                Map<String, Object> variables = p.getVariables();
                bo.setProcessInstanceId(p.getBusinessId());
                bo.setCurrentTaskId(p.getStepId());
                bo.setPackageId(getValue(variables, "packageId"));
                bo.setContentId(getValue(variables, "contentId"));
                bo.setTestId(getValue(variables, "testId"));
                bo.setTestName(getValue(variables, "testName"));
                bo.setTeacherId(getValue(variables, "sender"));
                bo.setTeacherName(getValue(variables, "senderName"));
                list.add(bo);
            }
        });
        return list;

    }

    /**
     * student report
     *
     * @param processInstanceId
     * @param studentId
     * @return
     */
    public AssessmentActionFlowBO studentReport(String processInstanceId, String studentId) {
        ActionFlowCompleteInfoDTO infoDTO = new ActionFlowCompleteInfoDTO();
        infoDTO.setBusinessId(processInstanceId);
        infoDTO.setStepKey("report_5");
        infoDTO.setUserId(studentId);
        ActionFlowTaskInfoDTO dto = actionFlowBaseService.complete(infoDTO);
        AssessmentActionFlowBO bo = new AssessmentActionFlowBO();
        Map<String, Object> variables = dto.getVariables();
        bo.setProcessInstanceId(dto.getBusinessId());
        bo.setCurrentTaskId(dto.getStepId());
        bo.setPackageId(getValue(variables, "packageId"));
        bo.setContentId(getValue(variables, "contentId"));
        bo.setTestId(getValue(variables, "testId"));
        bo.setTestName(getValue(variables, "testName"));
        bo.setTeacherId(getValue(variables, "sender"));
        bo.setTeacherName(getValue(variables, "senderName"));
        bo.setIsComplete(dto.getStatus());
        bo.setCreateTime(dto.getCreateTime());
        return bo;

    }


}
