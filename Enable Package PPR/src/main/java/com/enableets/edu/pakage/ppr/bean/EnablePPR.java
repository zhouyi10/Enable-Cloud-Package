package com.enableets.edu.pakage.ppr.bean;

import com.enableets.edu.pakage.card.bean.EnableCard;
import com.enableets.edu.pakage.card.bean.EnableCardBeanDefinition;
import com.enableets.edu.pakage.core.action.IPackageLifecycle;
import com.enableets.edu.pakage.core.bean.AbstractEnablePackageDecorator;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.source.FileIdSource;
import com.enableets.edu.pakage.core.source.PackageSource;
import com.enableets.edu.pakage.core.source.URLSource;
import com.enableets.edu.pakage.ppr.action.PPRPackageLifecycle;

/**
 * The PPR decorator extension class
 */
public class EnablePPR extends AbstractEnablePackageDecorator implements IPackageLifecycle {

    /** Build PPR Constructor*/
    public EnablePPR(EnablePPRBeanDefinition pprBeanDefinition, EnableCardBeanDefinition cardBeanDefinition, Configuration configuration){
        EnableCard enableCard = new EnableCard(cardBeanDefinition, configuration);
        this.packageWrapper = new PPRPackageWrapper(configuration, pprBeanDefinition, enableCard);
        this.packageLifecycle = new PPRPackageLifecycle(this.packageWrapper);
    }

    /** Parse PPR Constructor*/
    public EnablePPR(PackageFileInfo packageFileInfo, Configuration configuration){
        this.packageWrapper =  new PPRPackageWrapper(configuration, packageFileInfo);
        this.packageLifecycle =  new PPRPackageLifecycle(this.packageWrapper);
    }

    public EnablePPR(Configuration configuration){
        this.packageWrapper =  new PPRPackageWrapper(configuration);
        this.packageLifecycle =  new PPRPackageLifecycle(this.packageWrapper);
    }

    private PPRPackageWrapper packageWrapper;
    private PPRPackageLifecycle packageLifecycle;


    public EnablePPRBeanDefinition getEnablePPRBeanDefinition() {
        return packageWrapper.getPprBeanDefinition();
    }

    public EnableCard getEnableCard() {
        return this.packageWrapper.getEnableCard();
    }

    @Override
    public void pull(PackageSource source) {
        if(source instanceof URLSource){
            PackageFileInfo fileInfo = new PackageFileInfo();
            fileInfo.setDownloadUrl(((URLSource) source).getUrl());
            this.packageWrapper.setPackageFileInfo(fileInfo);
            packageLifecycle.downLoad();
        }else if(source instanceof FileIdSource){
            PackageFileInfo fileInfo = new PackageFileInfo();
            fileInfo.setFileId(((FileIdSource) source).getId());
            this.packageWrapper.setPackageFileInfo(fileInfo);
            packageLifecycle.downLoad();
        }
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
        return packageWrapper.getPprBeanDefinition().getPaperId();
    }

    @Override
    public void setConfiguration() {
         packageWrapper.getConfiguration();
    }
}
