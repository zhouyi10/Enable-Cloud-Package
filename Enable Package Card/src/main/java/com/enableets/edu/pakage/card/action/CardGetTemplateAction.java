package com.enableets.edu.pakage.card.action;

import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.card.bean.body.CardBody;
import com.enableets.edu.pakage.core.action.AbstractPackageAction;
import com.enableets.edu.pakage.core.core.Configuration;

/**
 * 提供获取答题卡模板的行为实现
 */
public class CardGetTemplateAction extends AbstractPackageAction {

    public CardGetTemplateAction(EnableCardPackage enableCardPackage, Configuration configuration) {
        super(enableCardPackage, configuration);
    }

    @Override
    public String getActionName() {
        return null;
    }

    @Override
    public Object execute() {
        CardBody body = (CardBody)this.getEnablePackage().getBody();
        return body.getLayout();
    }
}
