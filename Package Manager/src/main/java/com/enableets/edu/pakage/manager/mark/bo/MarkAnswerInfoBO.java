package com.enableets.edu.pakage.manager.mark.bo;

import java.util.List;

/**
 * 学生答题信息
 * @Author walle_yu@enable-ets.com
 * @since 2018/12/19
 */
public class MarkAnswerInfoBO {

    /** 考试标识*/
    private String testId;

    /** 考试标识*/
    private String examId;

    /** 考试名称*/
    private String testName;

    /** 交卷人数*/
    private Integer submitCount;

    /** 批阅状态*/
    private String markStatus;

    /** 0：暂存， 1：批阅*/
    private String type;

    /** 按学生分类*/
    private List<UserAnswerInfoBO> answers;

    /** 总人数*/
    private Integer totalCount;

    private String userId;

    private String questionId;

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Integer getSubmitCount() {
        return submitCount;
    }

    public void setSubmitCount(Integer submitCount) {
        this.submitCount = submitCount;
    }

    public String getMarkStatus() {
        return markStatus;
    }

    public void setMarkStatus(String markStatus) {
        this.markStatus = markStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<UserAnswerInfoBO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<UserAnswerInfoBO> answers) {
        this.answers = answers;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
