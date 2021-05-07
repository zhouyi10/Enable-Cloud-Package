package com.enableets.edu.pakage.card.action;

import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.core.core.Configuration;

/**
 * 提供答题卡的统一行为实现
 */
public class DefaultEnableCardActionResolver implements IEnableCardAction {

    private EnableCardPackage enableCardPackage;

    private Configuration configuration;

    public DefaultEnableCardActionResolver(EnableCardPackage enableCardPackage, Configuration configuration){
        this.enableCardPackage = enableCardPackage;
        this.configuration = configuration;
        this.cardSaveAction = new CardSaveAction(enableCardPackage, configuration);
        this.cardClockInStepAction = new CardClockInStepAction(enableCardPackage, configuration);
        this.cardCompleteAction = new CardCompleteAction(enableCardPackage, configuration);
        this.cardGetTemplateAction = new CardGetTemplateAction(enableCardPackage, configuration);
    }

    private CardSaveAction cardSaveAction;
    private CardClockInStepAction cardClockInStepAction;
    private CardCompleteAction cardCompleteAction;
    private CardGetTemplateAction cardGetTemplateAction;

    @Override
    public void save() {
        cardSaveAction.setAnswer(enableCardPackage.getBody().getAnswer());
        cardSaveAction.execute();
    }

    @Override
    public void clockIn() {
        cardClockInStepAction.setAction(enableCardPackage.getBody().getAction());
        cardClockInStepAction.execute();
    }

    @Override
    public void complete() {
        cardCompleteAction.setMark(enableCardPackage.getBody().getMark());
        cardCompleteAction.execute();
    }

    @Override
    public void getTemplate() {
        cardGetTemplateAction.execute();
    }
}
