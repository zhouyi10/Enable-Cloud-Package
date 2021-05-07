package com.enableets.edu.pakage.card.action;

import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.card.bean.body.CardBody;
import com.enableets.edu.pakage.card.bean.body.mark.Mark;
import com.enableets.edu.pakage.core.action.AbstractPackageAction;
import com.enableets.edu.pakage.core.core.Configuration;

/**
 * Save Mark Result
 */
public class CardCompleteAction extends AbstractPackageAction {

    private Mark mark;

    public CardCompleteAction(EnableCardPackage enableCardPackage, Configuration configuration) {
        super(enableCardPackage, configuration);
    }

    @Override
    public String getActionName() {
        return null;
    }

    @Override
    public Object execute() {
        CardBody body = (CardBody) this.getEnablePackage().getBody();
        body.setMark(mark);
        this.getEnablePackage().setBody(body);
        return this.getEnablePackage();
    }

    public void setMark(Mark mark){
        this.mark = mark;
    }
}
