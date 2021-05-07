package com.enableets.edu.pakage.ppr.bean;

import com.enableets.edu.pakage.card.bean.EnableCard;
import com.enableets.edu.pakage.core.bean.DefaultPackageWrapper;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.source.PackageSource;
import com.enableets.edu.pakage.ppr.bean.body.PPRBody;

/**
 * .ppr文件的包装类
 */
public class PPRPackageWrapper extends DefaultPackageWrapper<PPRBody> {
    private Configuration configuration;
    private EnablePPRBeanDefinition pprBeanDefinition;
    private EnableCard enableCard;
    private PackageSource packageSource;

    public PPRPackageWrapper(Configuration configuration, EnablePPRBeanDefinition pprBeanDefinition, EnableCard enableCard) {
        this.configuration = configuration;
        this.pprBeanDefinition = pprBeanDefinition;
        this.enableCard = enableCard;
    }

    public PPRPackageWrapper(Configuration configuration, PackageFileInfo packageFileInfo){
        this.configuration = configuration;
        setPackageFileInfo(packageFileInfo);
    }

    public PPRPackageWrapper(Configuration configuration){
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public EnablePPRBeanDefinition getPprBeanDefinition() {
        return pprBeanDefinition;
    }

    public void setPprBeanDefinition(EnablePPRBeanDefinition pprBeanDefinition) {
        this.pprBeanDefinition = pprBeanDefinition;
    }

    public EnableCard getEnableCard() {
        return enableCard;
    }

    public void setEnableCard(EnableCard enableCard) {
        this.enableCard = enableCard;
    }
}
