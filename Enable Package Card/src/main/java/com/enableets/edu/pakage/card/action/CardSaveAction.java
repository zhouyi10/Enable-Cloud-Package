package com.enableets.edu.pakage.card.action;

import com.enableets.edu.pakage.card.adapter.PPRServiceAdapter;
import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.card.bean.body.CardBody;
import com.enableets.edu.pakage.card.bean.body.answer.Answer;
import com.enableets.edu.pakage.core.action.AbstractPackageAction;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.utils.JsonUtils;

/**
 * Submit Answer Result
 */
public class CardSaveAction extends AbstractPackageAction {

    private Answer answer;

    public CardSaveAction(EnableCardPackage enableCardPackage, Configuration configuration) {
        super(enableCardPackage, configuration);
    }

    @Override
    public String getActionName() {
        return null;
    }

    @Override
    public Object execute() {
        CardBody body = (CardBody) this.getEnablePackage().getBody();
        body.setAnswer(answer);
        this.getEnablePackage().setBody(body);
        return PPRServiceAdapter.save(JsonUtils.convert(this.getEnablePackage()), getConfiguration());
    }

    public void setAnswer(Answer answer){
        this.answer = answer;
    }
}
