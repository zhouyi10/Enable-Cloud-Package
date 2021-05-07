package com.enableets.edu.sdk.ppr.ppr.director;

import org.apache.commons.collections.CollectionUtils;

import com.enableets.edu.sdk.ppr.adapter.PaperServiceAdapter;
import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.action.StepActionBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.answer.AnswerBO;
import com.enableets.edu.sdk.ppr.ppr.builder.handler.PaperCardBuilderHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/09/08
 **/
public class PaperCard implements IPaperCard{

    private Configuration configuration;

    private CardBO cardBO;

    private PaperCardBuilderHandler paperCardBuilderHandler = new PaperCardBuilderHandler();

    public PaperCard(CardBO cardBO, Configuration configuration){
        this.cardBO = cardBO;
        this.configuration = configuration;
    }

    /** */
    @Override
    public CardBO getCardTemplate(){
        return cardBO;
    }

    /** */
    @Override
    public void save(List<AnswerBO> answers) {
        cardBO.setAnswers(answers);
    }

    /** */
    @Override
    public void complete(List<StepActionBO> steps) {
        List<StepActionBO> actions = null;
        if (CollectionUtils.isNotEmpty(cardBO.getActions())){
            actions = cardBO.getActions();
        }else{
            actions = new ArrayList<>();
        }
        actions.addAll(steps);
        cardBO.setActions(actions);
    }

    /** */
    @Override
    public List<AnswerBO> getAnswer() {
        return cardBO.getAnswers();
    }

    /** */
    @Override
    public void complete() {
        PaperServiceAdapter.save(paperCardBuilderHandler.build(cardBO), configuration);
    }

}
