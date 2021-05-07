package com.enableets.edu.sdk.ppr.ppr.builder.handler;

import com.enableets.edu.sdk.ppr.ppr.bo.card.AnswerCardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.action.StepActionBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.answer.AnswerBO;
import com.enableets.edu.sdk.ppr.ppr.core.Item;
import com.enableets.edu.sdk.ppr.ppr.core.Property;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Action;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.AnswerCard;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Body;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Layout;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.PaperCardXML;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author walle_yu@enable-ets.com
 * @since 2020/07/01
 **/
public abstract class AbstractPaperCardBuilder {

    protected PaperCardXML paperCardXMl = new PaperCardXML();

    public CardBO cardBO;

    protected AbstractPaperCardBuilder(){}

    public AbstractPaperCardBuilder(CardBO cardBO){
        this.cardBO = cardBO;
    }

    public abstract void createHeader();

    public void createBody(){
        paperCardXMl.setBody(new Body(createLayout(cardBO.getAnswerCard()), createAction(cardBO.getActions()), createAnswerCard(cardBO.getAnswers())));
    }

    public abstract Layout createLayout(AnswerCardBO answerCard);

    public abstract Action createAction(List<StepActionBO> actions);

    public abstract AnswerCard createAnswerCard(List<AnswerBO> answers);

    public CardBO getPaperCard() {
        return cardBO;
    }

    public void setPaperCard(CardBO cardBO) {
        this.cardBO = cardBO;
    }

    public PaperCardXML getPaperCardXMl() {
        return paperCardXMl;
    }

}
