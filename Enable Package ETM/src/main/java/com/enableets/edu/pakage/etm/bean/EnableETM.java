package com.enableets.edu.pakage.etm.bean;

import com.enableets.edu.pakage.core.action.IPackageLifecycle;
import com.enableets.edu.pakage.core.bean.AbstractEnablePackageDecorator;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.source.PackageSource;
import com.enableets.edu.pakage.etm.action.ETMPackageLifecycle;


public class EnableETM extends AbstractEnablePackageDecorator implements IPackageLifecycle {

    public EnableETM(EnableETMBeanDefinition enableETMBeanDefinition, Configuration configuration) {

        this.packageWrapper = new ETMPackageWrapper(configuration,enableETMBeanDefinition);
        this.packageLifecycle = new ETMPackageLifecycle(this.packageWrapper);

    }

    public EnableETM(ETMPackageWrapper packageWrapper, ETMPackageLifecycle packageLifecycle) {
        this.packageWrapper = packageWrapper;
        this.packageLifecycle = packageLifecycle;
    }


    private ETMPackageWrapper packageWrapper;
    private ETMPackageLifecycle packageLifecycle;

    public EnableETMBeanDefinition getEnablePPRBeanDefinition() {
        return packageWrapper.getEnableETMBeanDefinition();
    }


    @Override
    public void pull(PackageSource source) {

    }

    @Override
    public void downLoad() {
        packageLifecycle.downLoad();
    }

    @Override
    public void parse() {
        packageLifecycle.parse();
    }

    @Override
    public void build() {
        packageLifecycle.build();
    }

    @Override
    public void destroy() {
        packageLifecycle.destroy();
    }


    @Override
    public String getId() {
        return packageWrapper.getEnableETMBeanDefinition().getBookId();
    }

    @Override
    public void setConfiguration() {
        packageWrapper.getConfiguration();
    }
}
