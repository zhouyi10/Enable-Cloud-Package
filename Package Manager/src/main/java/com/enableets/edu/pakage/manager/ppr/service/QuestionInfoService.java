package com.enableets.edu.pakage.manager.ppr.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.pakage.framework.core.Constants;
import com.enableets.edu.pakage.manager.core.BaseInfoService;
import com.enableets.edu.pakage.manager.ppr.bo.PaperQuestionAnswerBO;
import com.enableets.edu.pakage.manager.ppr.bo.PaperQuestionBO;
import com.enableets.edu.pakage.manager.ppr.bo.QueryQuestionBO;
import com.enableets.edu.pakage.manager.ppr.bo.QuestionKnowledgeInfoBO;
import com.enableets.edu.sdk.paper.dto.AnswerInfoDTO;
import com.enableets.edu.sdk.paper.dto.QueryQuestionChildInfoResultDTO;
import com.enableets.edu.sdk.paper.dto.QueryQuestionInfoDTO;
import com.enableets.edu.sdk.paper.dto.QueryQuestionInfoResultDTO;
import com.enableets.edu.sdk.paper.service.IQuestionInfoService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/17
 **/
@Service
public class QuestionInfoService {

    @Autowired
    private IQuestionInfoService questionInfoServiceSDK;

    @Autowired
    private BaseInfoService baseInfoService;

    /**
     * Query Question
     * @param question
     * @return
     */
    public List<PaperQuestionBO> query(QueryQuestionBO question){
        if (StringUtils.isNotBlank(question.getProviderCode()) && question.getProviderCode().equals(Constants.CONTENT_SCHOOL_TYPE)){
            question.setSchoolId(baseInfoService.getUserSchoolInfo(question.getCreator()).getId());
        }
        QueryQuestionInfoDTO queryQuestion = BeanUtils.convert(question, QueryQuestionInfoDTO.class);
        List<QueryQuestionInfoResultDTO> questions = questionInfoServiceSDK.query(queryQuestion);
        if (CollectionUtils.isEmpty(questions)) return new ArrayList<PaperQuestionBO>();
        return convertQuestion(questions);
    }

    /**
     *
     * @return
     */
    private List<PaperQuestionBO> convertQuestion(List<QueryQuestionInfoResultDTO> contentQuestions) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT);
        List<PaperQuestionBO> questions = new ArrayList<>();
        for (QueryQuestionInfoResultDTO contentQuestion : contentQuestions) {
            PaperQuestionBO question = new PaperQuestionBO();
            AnswerInfoDTO answer = contentQuestion.getAnswer();
            contentQuestion.setAnswer(null);
            if (CollectionUtils.isNotEmpty(contentQuestion.getChildren())){
                List<PaperQuestionBO> children = new ArrayList<>();
                for (QueryQuestionChildInfoResultDTO child : contentQuestion.getChildren()) {
                    AnswerInfoDTO childAnswer = child.getAnswer();
                    child.setAnswer(null);
                    PaperQuestionBO paperQuestionBO = BeanUtils.convert(child, PaperQuestionBO.class);
                    paperQuestionBO.setAnswer(new PaperQuestionAnswerBO(Long.valueOf(child.getQuestionId()), childAnswer.getLable(), childAnswer.getStrategy(), childAnswer.getAnalysis()));
                    children.add(paperQuestionBO);
                }
            }
            question = BeanUtils.convert(contentQuestion, PaperQuestionBO.class);
            if (CollectionUtils.isNotEmpty(contentQuestion.getKnowledgeList())){
                question.setKnowledges(BeanUtils.convert(contentQuestion.getKnowledgeList(), QuestionKnowledgeInfoBO.class));
            }
            question.setAnswer(new PaperQuestionAnswerBO(Long.valueOf(contentQuestion.getQuestionId()), answer.getLable(), answer.getStrategy(), answer.getAnalysis()));
            questions.add(question);
        }
        return questions;
    }

    /**
     * Count Question
     * @param question
     * @return
     */
    public Integer count(QueryQuestionBO question){
        if (StringUtils.isNotBlank(question.getProviderCode()) && question.getProviderCode().equals(Constants.CONTENT_SCHOOL_TYPE)){
            question.setSchoolId(baseInfoService.getUserSchoolInfo(question.getCreator()).getId());
        }
        QueryQuestionInfoDTO queryQuestion = BeanUtils.convert(question, QueryQuestionInfoDTO.class);
        return questionInfoServiceSDK.count(queryQuestion);
    }
}
