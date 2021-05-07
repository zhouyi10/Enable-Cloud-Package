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

    @Autowired
    private ITokenGenerator tokenGenerator;

    @Autowired
    private AnswerCardInfoDAO answerCardInfoDAO;

    @Autowired
    private AnswerCardAxisDAO answerCardAxisDAO;

    @Autowired
    private CardTimeAxisDAO cardTimeAxisDAO;

    @Transactional
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
    @Transactional
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
}
