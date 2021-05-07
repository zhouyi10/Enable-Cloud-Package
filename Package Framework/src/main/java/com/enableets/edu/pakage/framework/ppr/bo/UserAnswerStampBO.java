package com.enableets.edu.pakage.framework.ppr.bo;

import java.util.Date;

/**
 * @author duffy_ding
 * @since 2019/09/29
 */
public class UserAnswerStampBO {

    /** 主键标识 */
    private String answerStampId;

    /** 答案标识 */
    private String answerId;

    /** 开始时间 */
    private Date beginTime;

    /** 结束时间 */
    private Date endTime;

    /** 作答时长 */
    private Long lastTime;

    /** 题目标识 */
    private String questionId;

    /** 题号 */
    private String questionNo;

    /** 测验标识 */
    private String testId;

    /** 试卷标识 */
    private String examId;

    /** 创建人 */
    private String creator;

    /** 创建时间 */
    private Date createTime;

    /** 更新人 */
    private String updator;

    /** 更新时间 */
    private Date updateTime;

    public String getAnswerStampId() {
        return answerStampId;
    }

    public void setAnswerStampId(String answerStampId) {
        this.answerStampId = answerStampId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
