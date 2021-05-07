package com.enableets.edu.pakage.manager.ppr.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.Data;

/**
 * Student Submit AnswerCard
 * @author walle_yu@enable-ets.com
 * @since 2020/11/23
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerCardVO implements java.io.Serializable{

    private String testId;

    private String paperId;

    private String stepId;

    private String userId;

    private String startTime;

    private String endTime;

    private List<AnswerInfoVO> answers;

    public static class AnswerInfoVO{

        private String questionId;

        private String parentId;

        private String userAnswer;

        private List<String> answerStamp;

        private List<AnswerCanvas> canvases;

        private String answerCostTime;

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        public List<String> getAnswerStamp() {
            return answerStamp;
        }

        public void setAnswerStamp(List<String> answerStamp) {
            this.answerStamp = answerStamp;
        }

        public List<AnswerCanvas> getCanvases() {
            return canvases;
        }

        public void setCanvases(List<AnswerCanvas> canvases) {
            this.canvases = canvases;
        }

        public String getAnswerCostTime() {
            return answerCostTime;
        }

        public void setAnswerCostTime(String answerCostTime) {
            this.answerCostTime = answerCostTime;
        }
    }

    public static class AnswerCanvas{

        private String fileId;

        private String fileName;

        private String url;

        private String md5;

        private Integer order;

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }
    }
}
