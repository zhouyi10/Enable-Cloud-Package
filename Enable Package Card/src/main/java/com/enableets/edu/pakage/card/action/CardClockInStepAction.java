package com.enableets.edu.pakage.card.action;

import com.enableets.edu.pakage.card.bean.body.CardBody;
import com.enableets.edu.pakage.card.bean.body.action.Action;
import com.enableets.edu.pakage.core.action.AbstractPackageAction;
import com.enableets.edu.pakage.core.bean.AbstractEnablePackage;
import com.enableets.edu.pakage.core.core.Configuration;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/23
 **/
public class CardClockInStepAction extends AbstractPackageAction {

    private Action action;

    public CardClockInStepAction(AbstractEnablePackage enablePackage, Configuration configuration) {
        super(enablePackage, configuration);
    }

    @Override
    public String getActionName() {
        return null;
    }

    @Override
    public Object execute() {
        CardBody body = (CardBody) this.getEnablePackage().getBody();
        body.setAction(action);
        this.getEnablePackage().setBody(body);
        return this.getEnablePackage();
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
