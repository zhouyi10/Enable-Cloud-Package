package com.enableets.edu.pakage.framework.ppr.test.service.submit.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.enableets.edu.pakage.framework.ppr.test.service.submit.bo.PPRAnswerInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperNodeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperQuestionAnswerBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperQuestionBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestUserInfoBO;
import com.enableets.edu.pakage.framework.ppr.core.AutoMarkStrategyConfig;

import cn.hutool.core.util.ObjectUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutoMarkStrategyUtils {

    /** log printing class */
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoMarkStrategyUtils.class);

    public static final String ANSWER_STATUS_RIGHT = "0";

    public static final String ANSWER_STATUS_WRONG = "1";

    public static final String MARK_STATUS_UN_MARK = "0";

    public static final String MARK_STATUS_MARKED = "1";

    private static final Float DEFAULT_SCORE = 0f;


    /**
     *  Calculate the total score of the student's paper submission and set the status of the paper submission
     * @param userAnswers Answer List
     */
    public static TestUserInfoBO resetUserMarkStatus(List<PPRAnswerInfoBO> userAnswers) {
        TestUserInfoBO testUser = new TestUserInfoBO();
        float allScore = 0;    // Total score
        boolean theMarked = true;    // All review status parameters, true review all, false not review all
        for(PPRAnswerInfoBO answer : userAnswers){
            if (StringUtils.isBlank(answer.getMarkStatus())) {
                answer.setAnswerStatus(ANSWER_STATUS_RIGHT);
            }
            if (theMarked && MARK_STATUS_UN_MARK.equals(answer.getMarkStatus())) {
                theMarked = false;
            }
            try {
                allScore += answer.getAnswerScore();
            } catch (NumberFormatException e) {
                LOGGER.error("score format error, {}", e.getMessage());
            }
        }
        if (theMarked) {
            testUser.setMarkStatus(MARK_STATUS_MARKED);
        } else {
            testUser.setMarkStatus(MARK_STATUS_UN_MARK);
        }
        testUser.setUserScore(allScore);
        return testUser;
    }



    /**
     * Review questions
     * @param userAnswer User Answer
     * @param questionNode Question Node Info
     */
    public static void mark(PPRAnswerInfoBO userAnswer, PaperNodeInfoBO questionNode) {

        userAnswer.setAnswerScore(DEFAULT_SCORE);   //Set a default score of 0
        userAnswer.setAnswerStatus(ANSWER_STATUS_WRONG);  //Set default wrong answer
        userAnswer.setMarkStatus(MARK_STATUS_MARKED);  //Marked
        if (StringUtils.isBlank(userAnswer.getAnswer())){
            userAnswer.setAnswer("[\"\"]");
            return;
        }else{
            userAnswer.setAnswer(userAnswer.getAnswer().replace("&quot;", "\""));
        }
        if (questionNode == null || questionNode.getQuestion().getAnswer()== null || StringUtils.isBlank(questionNode.getQuestion().getAnswer().getLabel()) || !AutoMarkStrategyConfig.canAutoMark(questionNode.getQuestion().getType().getCode())){
            userAnswer.setMarkStatus(MARK_STATUS_UN_MARK);  //UnMarked
            return;
        }
        PaperQuestionBO question = questionNode.getQuestion();
        String questionType = question.getType().getCode();
        if (AutoMarkStrategyConfig.isSingleChoose(questionType)) {	// Single Choice Question
            doSingleChoiceQuestionMark(userAnswer, questionNode);
        } else if (AutoMarkStrategyConfig.isMultiChoose(questionType)) {  // Multiple Choice Question
            doMultipleChoiceQuestionMark(userAnswer, questionNode);
        } else if (AutoMarkStrategyConfig.isJudge(questionType)) {	// Judgment Question
            doJudgmentQuestionMark(userAnswer, questionNode);
        } else if (AutoMarkStrategyConfig.isDrag(questionType)) {	// Drag question
            doDragQuestionMark(userAnswer, questionNode);
        } else {
			doBlankQuestionMark(userAnswer, questionNode);    // Fill in the blank,All other types are also handled as fill in the blanks
        }
    }

    private static void doDragQuestionMark(PPRAnswerInfoBO userAnswer, PaperNodeInfoBO questionNode) {
        String userAnswerStr = userAnswer.getAnswer();
        if (ObjectUtil.isNotNull(questionNode.getQuestion().getAnswer().getLabel()) && ObjectUtil.isNotNull(userAnswerStr)) {
            String[] questionAnswerArray = questionNode.getQuestion().getAnswer().getLabel().split("@#@");
            String[] userAnswerArray = userAnswerStr.split(",");
            if (questionAnswerArray.length == userAnswerArray.length) {
                if (questionNode.getPoints() > 0) {
                    Float avgPoint = questionNode.getPoints()/questionAnswerArray.length;
                    Float userPoint = 0f;
                    int perfectSum = 0;
                    for (int i = 0; i < questionAnswerArray.length; i++) {
                        if (StringUtils.isNotBlank(questionAnswerArray[i]) && StringUtils.isNotBlank(userAnswerArray[i]) &&
                                Integer.valueOf(questionAnswerArray[i]).intValue() == Integer.valueOf(userAnswerArray[i]).intValue()) {
                            userPoint += avgPoint;
                            perfectSum ++;
                        }
                    }

                    // A score of more than 80% means the answer is correct
                    if (userPoint <= questionNode.getPoints() * 0.8){
                        userAnswer.setAnswerStatus(ANSWER_STATUS_WRONG);
                    }else userAnswer.setAnswerStatus(ANSWER_STATUS_RIGHT);

                    if (perfectSum == questionAnswerArray.length) {
                        userAnswer.setAnswerScore(questionNode.getPoints());
                    } else {
                        userAnswer.setAnswerScore(Math.round(userPoint * 100)/100f);
                    }
                    userAnswer.setMarkStatus("1");
                }
            }
        }
    }

    /**
     * Objective question review
     * @param userAnswer
     * @param questionNode
     */
    private static void doBlankQuestionMark(PPRAnswerInfoBO userAnswer, PaperNodeInfoBO questionNode) {
        userAnswer.setMarkStatus(MARK_STATUS_UN_MARK);  //Unreviewed
        // Answers with pictures do not review
        PaperQuestionAnswerBO answer = questionNode.getQuestion().getAnswer();
        if (StringUtils.isNotBlank(answer.getStrategy()) && answer.getStrategy().indexOf("<img") >= 0  && !AutoMarkStrategyConfig.isCollectLine(questionNode.getQuestion().getType().getCode())) {
            return;
        }
        // Do not review if there is drawing information
        if (!CollectionUtils.isEmpty(userAnswer.getCanvases())) {
            return;
        }
        if (answer.getLabel().contains("[answerType2]")){
            //Fill in the blanks
            doNoOrderBlankQuestionMark(userAnswer, questionNode);
        }else{
            //Orderly fill in the blanks
            doBlankDefaultQuestionMark(userAnswer,questionNode);
        }
    }

    /**
     * Orderly fill in the blanks
     * @param userAnswer
     * @param questionNode
     */
    private static void doBlankDefaultQuestionMark(PPRAnswerInfoBO userAnswer, PaperNodeInfoBO questionNode) {
        /**
         * 1. Parse the correct answer
         * 2. Judge Score Main logical structure
         * 3. Last change of question status
         */
        // 1
        List<List<String>> answerGroups = new ArrayList<>();
        // notice：Add spaces at both ends ，Do not add "@#@", The divided value is an empty array
        String lable = " " + StringUtils.defaultIfBlank(questionNode.getQuestion().getAnswer().getLabel(), "") + " ";
        String[] answerStrGroups = lable.split("@\\*@");
        for (String answerStrGroup : answerStrGroups) {
            answerGroups.add(Arrays.asList((" " + answerStrGroup + " ").split("@#@")));
        }

        // 2
        int rightCount = 0;	// Correct Answer Quantity
        int blankCount = answerGroups.get(0).size(); //Number of blanks
        String[] userAnswerArr = getAnswerArray(userAnswer.getAnswer());
        for (List<String> answerGroup : answerGroups) {
            int validCount = Math.min(answerGroup.size(), userAnswerArr.length);
            if (validCount == 0) {
                continue;
            }
            int tempRightCount = 0;
            for (int i = 0; i < validCount; i++) {
                if (answerGroup.get(i).trim().equals(userAnswerArr[i].trim())) {
                    tempRightCount++;
                }
            }
            rightCount = Math.max(rightCount, tempRightCount);
            if (rightCount == blankCount) { //All answers are correct, no need to compare
                break;
            }
        }

        // 3
        if (rightCount == blankCount) {
            userAnswer.setAnswerScore(questionNode.getPoints());
            userAnswer.setAnswerStatus(ANSWER_STATUS_RIGHT);
            userAnswer.setMarkStatus(MARK_STATUS_MARKED); // Objective questions answered correctly, set as reviewed
        } else {
            double everyScore = Math.floor(questionNode.getPoints() * 10 / blankCount) / 10;
            userAnswer.setAnswerScore((float)everyScore * rightCount);
            if (AutoMarkStrategyConfig.isCollectLine(questionNode.getQuestion().getType().getCode())){
                if (everyScore * rightCount <= questionNode.getPoints() * 0.8){
                    userAnswer.setAnswerStatus(ANSWER_STATUS_WRONG);
                }else userAnswer.setAnswerStatus(ANSWER_STATUS_RIGHT);
                userAnswer.setMarkStatus(MARK_STATUS_MARKED); // Objective questions answered correctly, set as reviewed
            }
        }
    }

    /**
     * Unordered fill in the blank questions review
     * @param userAnswer User answer information
     * @param questionNode Topic node information
     */
    private static void doNoOrderBlankQuestionMark(PPRAnswerInfoBO userAnswer, PaperNodeInfoBO questionNode) {
        /**
         * 1. data handle
         * 2. The main logical structure of the score
         * 3. Last change the status of the question
         */
        // 1
        String questionAnswer = questionNode.getQuestion().getAnswer().getLabel().substring(0,
                questionNode.getQuestion().getAnswer().getLabel().indexOf("[answerType2]"));

        List<String> stuAnswerList = new ArrayList<>();
        String[] userAnswerObj = getAnswerArray(userAnswer.getAnswer());
        for (int i = 0; i < userAnswerObj.length; i++) {
            if (!stuAnswerList.contains(userAnswerObj[i].trim())) {
                stuAnswerList.add(userAnswerObj[i].trim());
            }
        }
        String[] answerArr = questionAnswer.split("@#@");
        float userScore = 0;
        int rightCount = 0;
        int questionCount = answerArr.length;
        //多空的全面取平均分去位数的，其余给给最后一个答案
        double everyScore = Math.floor((questionNode.getPoints() * 10)/ questionCount) / 10;
        // 2
        for (int i = 0; i < stuAnswerList.size(); i++) {
            for (int j = 0; j < answerArr.length; j++) {
                if (stuAnswerList.get(i).trim().equals(answerArr[j].trim())) {
                    rightCount++;
                    if (questionCount == rightCount) {
                        userScore = questionNode.getPoints();
                    } else {
                        userScore += everyScore;
                    }
                }
            }
        }
        //全正确给全分
        if (rightCount == answerArr.length) userScore = questionNode.getPoints();

        // 3
        if (rightCount == questionCount) {
            userAnswer.setAnswerStatus(ANSWER_STATUS_RIGHT);
            userAnswer.setMarkStatus(MARK_STATUS_MARKED); // 客观题 答对 设置为已批阅
        }
        userAnswer.setAnswerScore(userScore);

    }

    /**
     * 判断题批阅,与单选判断逻辑相同
     * @param userAnswer 用户答案信息
     * @param questionNode 题目节点信息
     */
    private static void doJudgmentQuestionMark(PPRAnswerInfoBO userAnswer, PaperNodeInfoBO questionNode) {
        doSingleChoiceQuestionMark(userAnswer, questionNode);
    }

    /**
     * 多选题批阅
     * @param userAnswer 用户答案信息
     * @param questionNode 题目节点信息
     */
    private static void doMultipleChoiceQuestionMark(PPRAnswerInfoBO userAnswer, PaperNodeInfoBO questionNode) {
        Set<String> userAnswerSet = new HashSet<>(Arrays.asList(getAnswerArray(userAnswer.getAnswer())));
        if (CollectionUtils.isEmpty(userAnswerSet)) {
            return;
        }

        Set<String> questionAnswerSet = new HashSet<>(Arrays.asList(questionNode.getQuestion().getAnswer().getLabel().split(",")));
        if (questionAnswerSet.containsAll(userAnswerSet)) {
            if (questionAnswerSet.size() == userAnswerSet.size()) {
                userAnswer.setAnswerStatus(ANSWER_STATUS_RIGHT);
                userAnswer.setAnswerScore(questionNode.getPoints());
            }
        }
    }

    /**
     * 选择题批阅
     * @param userAnswer 用户答案信息
     * @param questionNode 题目节点信息
     */
    private static void doSingleChoiceQuestionMark(PPRAnswerInfoBO userAnswer, PaperNodeInfoBO questionNode) {
        String[] userAnswerArr = getAnswerArray(userAnswer.getAnswer());
        if (userAnswerArr.length > 0 && userAnswerArr[0].equals(questionNode.getQuestion().getAnswer().getLabel().trim())){
            userAnswer.setAnswerStatus(ANSWER_STATUS_RIGHT);  //答对
            userAnswer.setAnswerScore(questionNode.getPoints());
        }
        userAnswer.setMarkStatus(MARK_STATUS_MARKED);
    }

    /**
     * 获取答案信息（该解码逻辑来源于html中，应该(其他段答案类型未测试矫正)适用于所有答案）
     * @param answer 答案
     * @return 答案数组
     */
    private static String[] getAnswerArray(String answer) {
        if (StringUtils.isBlank(answer)) {
            return new String[0];
        }
        String str = answer.replaceAll("&apos;", "'")
                .replaceAll("&gt;", ">")
                .replaceAll("&lt;", "<")
                .replaceAll("&quot;", "\"")
                .replaceAll("&amp;", "&");
        if (str.startsWith("[") && str.endsWith("]")) {
            String s = str.replace("[", "").replace("]", "").replaceAll("\"", "").replaceAll("'", "");
            try {
                return s.split(",");
            } catch (Exception e) {
                LOGGER.error("answer {0} format error!", str);
            }
        }
        return str.split(",");
    }
}
