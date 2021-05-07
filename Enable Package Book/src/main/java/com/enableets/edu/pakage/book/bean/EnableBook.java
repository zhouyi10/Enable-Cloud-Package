package com.enableets.edu.pakage.book.bean;

import com.enableets.edu.pakage.book.action.BookPackageLifecycle;
import com.enableets.edu.pakage.core.action.IPackageLifecycle;
import com.enableets.edu.pakage.core.bean.AbstractEnablePackageDecorator;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.source.PackageSource;


public class EnableBook extends AbstractEnablePackageDecorator implements IPackageLifecycle {

    public EnableBook(EnableBookBeanDefinition enableBookBeanDefinition, Configuration configuration) {

        this.packageWrapper = new BookPackageWrapper(configuration,enableBookBeanDefinition);
        this.packageLifecycle = new BookPackageLifecycle(this.packageWrapper);

    }

    public EnableBook(BookPackageWrapper packageWrapper, BookPackageLifecycle packageLifecycle) {
        this.packageWrapper = packageWrapper;
        this.packageLifecycle = packageLifecycle;
    }


    private BookPackageWrapper packageWrapper;
    private BookPackageLifecycle packageLifecycle;

    public EnableBookBeanDefinition getEnablePPRBeanDefinition() {
        return packageWrapper.getEnableBookBeanDefinition();
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
        return packageWrapper.getEnableBookBeanDefinition().getBookId();
    }

    @Override
    public void setConfiguration() {
        packageWrapper.getConfiguration();
    }
}
