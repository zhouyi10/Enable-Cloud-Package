package com.enableets.edu.pakage.framework.ppr.test.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.framework.core.util.token.ITokenGenerator;
import com.enableets.edu.pakage.framework.ppr.test.dao.AnswerCardAxisDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.AnswerCardInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.CardTimeAxisDAO;
import com.enableets.edu.pakage.framework.ppr.test.po.AnswerCardAxisPO;
import com.enableets.edu.pakage.framework.ppr.test.po.AnswerCardInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.CardTimeAxisPO;
import com.enableets.edu.pakage.framework.ppr.bo.AnswerCardAxisBO;
import com.enableets.edu.pakage.framework.ppr.bo.AnswerCardInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.CardTimeAxisBO;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import tk.mybatis.mapper.entity.Example;

/**
 * Answer Card Service
 * @author walle_yu@enable-ets.com
 * @since 2020/10/23
 **/
@Service
public class AnswerCardInfoService {

    /** constant field */
    public static final String MAP_KEY_ANSWER_CARD_ID = "answerCardId";
    public static final String MAP_KEY_EXAM_ID = "examId";
    public static final String MAP_KEY_CREATOR = "creator";
    public static final String MAP_KEY_NODE_ID = "nodeId";
    public static final String MAP_KEY_SEQUENCING = "sequencing";
    public static final String MAP_KEY_CREATE_TIME = "createTime";


    @Autowired
    private ITokenGenerator tokenGenerator;

    @Autowired
    private AnswerCardInfoDAO answerCardInfoDAO;

    @Autowired
    private AnswerCardAxisDAO answerCardAxisDAO;

    @Autowired
    private CardTimeAxisDAO cardTimeAxisDAO;

    @Transactional(value = "packageTransactionManager")
    public AnswerCardInfoBO add(AnswerCardInfoBO info) {
        Assert.notNull(info, "info cannot be empty!");
        Assert.notNull(info.getExamId(), "examId cannot be empty!");
        Assert.hasText(info.getCreator(), "creator cannot be empty!");

        // 1. delete the original answer sheet information
        remove(null, info.getExamId(), info.getCreator());
        // 2. set primary key
        info.setAnswerCardId((Long)tokenGenerator.getToken());
        info.setStatus(0);
        Date curTime = Calendar.getInstance().getTime();
        info.setCreateTime(curTime);
        info.setUpdateTime(curTime);
        info.setUpdator(info.getCreator());
        // 3. save answer card information
        answerCardInfoDAO.insertSelective(BeanUtils.convert(info, AnswerCardInfoPO.class));
        if(info.getAxises() != null){
            for (AnswerCardAxisBO axis : info.getAxises()) {
                axis.setAxisId((Long)tokenGenerator.getToken());
                axis.setAnswerCardId(info.getAnswerCardId());
                axis.setCreateTime(curTime);
                axis.setUpdateTime(curTime);
                axis.setCreator(info.getCreator());
                axis.setUpdator(info.getCreator());
            }
            // 4. save answer card coordinate information
            saveAxisData(info.getAxises());
        }
        if(info.getTimelines() !=null){
            for (CardTimeAxisBO timeline : info.getTimelines()) {
                timeline.setTimelineId((Long)tokenGenerator.getToken());
                timeline.setAnswerCardId(info.getAnswerCardId());
                timeline.setCreateTime(curTime);
                timeline.setUpdateTime(curTime);
                timeline.setCreator(info.getCreator());
                timeline.setUpdator(info.getCreator());
            }
            // 5. save answer card timeline information
            saveTimeline(info.getTimelines());
        }
        return info;
    }

    public void saveAxisData(List<AnswerCardAxisBO> axisInfoList) {
        if (CollectionUtils.isEmpty(axisInfoList)) {
            return;
        }
        answerCardAxisDAO.insertList(BeanUtils.convert(axisInfoList, AnswerCardAxisPO.class));
    }

    public void saveTimeline(List<CardTimeAxisBO> timelineList) {
        if (CollectionUtils.isEmpty(timelineList)) {
            return;
        }
        cardTimeAxisDAO.insertList(BeanUtils.convert(timelineList, CardTimeAxisPO.class));
    }

    /**
     * delete answer card and coordinate information
     * @param answerCardId answer card id
     * @param examId exam id
     * @param creator creator
     * @return true/false
     */
    @Transactional(value = "packageTransactionManager")
    public boolean remove(Long answerCardId, Long examId, String creator) {
        Example example = new Example(AnswerCardInfoPO.class);
        Example.Criteria criteria = example.createCriteria();
        if (answerCardId != null) {
            criteria.andEqualTo("answerCardId", answerCardId);
        }
        if (examId != null) {
            criteria.andEqualTo("examId", examId);
        }
        if (StringUtils.isNotBlank(creator)) {
            criteria.andEqualTo("creator", creator);
        }
        AnswerCardInfoPO param = new AnswerCardInfoPO();
        param.setStatus(1);
        answerCardInfoDAO.updateByExampleSelective(param, example);
        return Boolean.TRUE;
    }

    /**
     * 查询 ppr对应的答题卡
     * @param id
     * @param userId
     * @return
     */
    public AnswerCardInfoBO getAnswerCard(String id, String userId) {
        Example example = new Example(AnswerCardInfoPO.class);
        example.createCriteria().andEqualTo("examId", Long.valueOf(id));
        example.createCriteria().andEqualTo("status", "0");
        example.createCriteria().andEqualTo("creator", userId);
        List<AnswerCardInfoPO> answerCards = answerCardInfoDAO.selectByExample(example);
        if (CollectionUtils.isNotEmpty(answerCards)) {
            AnswerCardInfoBO answerCard = BeanUtils.convert(answerCards.get(0), AnswerCardInfoBO.class);
            AnswerCardAxisPO axis = new AnswerCardAxisPO();
            CardTimeAxisPO timeline = new CardTimeAxisPO();
            axis.setAnswerCardId(answerCard.getAnswerCardId());
            timeline.setAnswerCardId(answerCard.getAnswerCardId());
            List<AnswerCardAxisPO> axises = answerCardAxisDAO.select(axis);
            List<CardTimeAxisPO> timelines = cardTimeAxisDAO.select(timeline);
            answerCard.setAxises(BeanUtils.convert(axises, AnswerCardAxisBO.class));
            answerCard.setTimelines(BeanUtils.convert(timelines, CardTimeAxisBO.class));
            return answerCard;
        }
        return null;
    }

    @Transactional(value = "packageTransactionManager")
    public List<AnswerCardInfoBO> query(Long examId, String creator){
        Assert.notNull(examId, "examId cannot be empty!");
        Example example = new Example(AnswerCardInfoPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(MAP_KEY_EXAM_ID, examId).andEqualTo("status", 0);
        if (StringUtils.isNotBlank(creator)) {
            criteria.andEqualTo(MAP_KEY_CREATOR, creator);
        }
        example.orderBy(MAP_KEY_CREATE_TIME).desc();
        List<AnswerCardInfoPO> pos = answerCardInfoDAO.selectByExample(example);
        if (CollectionUtils.isEmpty(pos)) {
            return Collections.emptyList();
        }
        List<AnswerCardInfoBO> result = BeanUtils.convert(pos, AnswerCardInfoBO.class);
        for (AnswerCardInfoBO answerCardInfoBO : result) {
            answerCardInfoBO.setAxises(queryAxises(answerCardInfoBO.getAnswerCardId()));
        }
        return result;
    }

    /**
     * Query the answer sheet coordinate information according to the answer sheet ID {@code Long}
     * @param answerCardId answer card id
     * @return answer sheet coordinate information
     */
    private List<AnswerCardAxisBO> queryAxises(Long answerCardId) {
        Example example = new Example(AnswerCardAxisPO.class);
        example.createCriteria().andEqualTo(MAP_KEY_ANSWER_CARD_ID, answerCardId);
        example.orderBy(MAP_KEY_NODE_ID).asc();
        example.orderBy(MAP_KEY_SEQUENCING).asc();
        List<AnswerCardAxisPO> pos = answerCardAxisDAO.selectByExample(example);
        if (CollectionUtils.isEmpty(pos)) {
            return Collections.emptyList();
        }
        return BeanUtils.convert(pos, AnswerCardAxisBO.class);
    }
}
