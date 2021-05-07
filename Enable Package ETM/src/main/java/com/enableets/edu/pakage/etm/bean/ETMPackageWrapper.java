package com.enableets.edu.pakage.etm.bean;

import com.enableets.edu.pakage.core.bean.DefaultPackageWrapper;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.etm.bean.body.ETMBody;

public class ETMPackageWrapper extends DefaultPackageWrapper<ETMBody> {

    private Configuration configuration;
    private EnableETMBeanDefinition enableETMBeanDefinition;

    public ETMPackageWrapper(Configuration configuration, EnableETMBeanDefinition enableETMBeanDefinition) {
        this.configuration = configuration;
        this.enableETMBeanDefinition = enableETMBeanDefinition;
    }

    public ETMPackageWrapper(Configuration configuration, PackageFileInfo packageFileInfo){
        this.configuration = configuration;
        setPackageFileInfo(packageFileInfo);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public EnableETMBeanDefinition getEnableETMBeanDefinition() {
        return enableETMBeanDefinition;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setEnableETMBeanDefinition(EnableETMBeanDefinition enableETMBeanDefinition) {
        this.enableETMBeanDefinition = enableETMBeanDefinition;
    }
}
