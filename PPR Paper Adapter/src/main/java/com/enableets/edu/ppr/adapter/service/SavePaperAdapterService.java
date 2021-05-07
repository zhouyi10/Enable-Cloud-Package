package com.enableets.edu.ppr.adapter.service;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enableets.edu.ppr.adapter.bo.SaveExamBO;
import com.enableets.edu.ppr.adapter.dao.ExamDAO;
import com.enableets.edu.ppr.adapter.dao.ExamKindDAO;
import com.enableets.edu.ppr.adapter.dao.ExamQuestionChildDAO;
import com.enableets.edu.ppr.adapter.dao.ExamQuestionDAO;
import com.enableets.edu.ppr.adapter.dao.ExamQuestionTypeDAO;
import com.enableets.edu.ppr.adapter.po.ExamPO;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/09/22
 **/
@Service
public class SavePaperAdapterService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SavePaperAdapterService.class);

    @Autowired
    private ExamDAO examDAO;

    @Autowired
    private ExamKindDAO examKindDAO;

    @Autowired
    private ExamQuestionTypeDAO examQuestionTypeDAO;

    @Autowired
    private ExamQuestionDAO examQuestionDAO;

    @Autowired
    private ExamQuestionChildDAO examQuestionChildDAO;

    @Transactional
    public void save(SaveExamBO examBO){
        if (examBO == null) return;
        ExamPO exam = examBO.getExam();
        exam.setSubjectId(exam.getSubjectCode());
        exam.setGradeId(exam.getGradeCode());
        examDAO.insertSelective(exam);
        examKindDAO.insertList(examBO.getExamKinds());
        examQuestionTypeDAO.insertList(examBO.getExamQuestionTypes());
        examQuestionDAO.insertList(examBO.getExamQuestions());
        if (CollectionUtils.isNotEmpty(examBO.getExamQuestionChildren()))
            examQuestionChildDAO.insertList(examBO.getExamQuestionChildren());
    }

}
