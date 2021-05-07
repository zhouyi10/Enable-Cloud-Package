package com.enableets.edu.sdk.ppr.ppr.director;

import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.action.StepActionBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.answer.AnswerBO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/09/16
 **/
public interface IPaperCard {

    public CardBO getCardTemplate();

    public void save(List<AnswerBO> answers);

    /**Save Answer Result*/
    public void complete(List<StepActionBO> steps);

    public List<AnswerBO> getAnswer();
//
//    /**Save Mark Result */
//    public void save(List<StepActionBO> steps);

    /** Complete Mark*/
    public void complete();
}
