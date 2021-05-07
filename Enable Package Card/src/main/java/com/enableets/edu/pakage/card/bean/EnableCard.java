package com.enableets.edu.pakage.card.bean;

import com.enableets.edu.pakage.card.action.DefaultEnableCardActionResolver;
import com.enableets.edu.pakage.card.action.IEnableCardAction;
import com.enableets.edu.pakage.core.core.Configuration;

/**
 * 答题卡的装饰器扩展类
 */
public class EnableCard implements IEnableCardAction {

    private EnableCardBeanDefinition cardBeanDefinition;

    private Configuration configuration;

    private DefaultEnableCardActionResolver cardActionResolver;

    private EnableCardPackage enableCardPackage;

    public EnableCard(EnableCardBeanDefinition cardBeanDefinition, Configuration configuration){
        this.cardBeanDefinition = cardBeanDefinition;
        this.configuration = configuration;
    }

    public EnableCard(EnableCardPackage enableCardPackage, Configuration configuration){
        this.enableCardPackage = enableCardPackage;
        this.configuration = configuration;
        this.cardActionResolver = new DefaultEnableCardActionResolver(enableCardPackage, configuration);
    }

    public void setEnableCardPackage(EnableCardPackage enableCardPackage){
        this.enableCardPackage = enableCardPackage;
        this.cardActionResolver = new DefaultEnableCardActionResolver(enableCardPackage, configuration);
    }

    public EnableCardBeanDefinition getCardBeanDefinition() {
        return cardBeanDefinition;
    }

    public DefaultEnableCardActionResolver getCardActionResolver() {
        return cardActionResolver;
    }

    public EnableCardPackage getEnableCardPackage() {
        return enableCardPackage;
    }

    @Override
    public void save() {
        cardActionResolver.save();
    }

    @Override
    public void clockIn() {
        cardActionResolver.clockIn();
    }

    @Override
    public void complete() {
        cardActionResolver.complete();
    }

    @Override
    public void getTemplate() {
        cardActionResolver.getTemplate();
    }
}
