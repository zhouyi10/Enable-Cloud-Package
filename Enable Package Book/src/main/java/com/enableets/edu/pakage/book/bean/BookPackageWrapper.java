package com.enableets.edu.pakage.book.bean;

import com.enableets.edu.pakage.book.bean.body.BookBody;
import com.enableets.edu.pakage.core.bean.DefaultPackageWrapper;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.core.Configuration;

public class BookPackageWrapper extends DefaultPackageWrapper<BookBody> {

    private Configuration configuration;
    private EnableBookBeanDefinition enableBookBeanDefinition;

    public BookPackageWrapper(Configuration configuration, EnableBookBeanDefinition enableBookBeanDefinition) {
        this.configuration = configuration;
        this.enableBookBeanDefinition = enableBookBeanDefinition;
    }

    public BookPackageWrapper(Configuration configuration, PackageFileInfo packageFileInfo){
        this.configuration = configuration;
        setPackageFileInfo(packageFileInfo);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public EnableBookBeanDefinition getEnableBookBeanDefinition() {
        return enableBookBeanDefinition;
    }

    public void setEnableBookBeanDefinition(EnableBookBeanDefinition enableBookBeanDefinition) {
        this.enableBookBeanDefinition = enableBookBeanDefinition;
    }
}
