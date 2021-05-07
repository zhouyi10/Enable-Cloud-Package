package com.enableets.edu.pakage.core.bean;

import com.enableets.edu.pakage.core.action.DefaultPackageLifecycle;
import com.enableets.edu.pakage.core.action.IPackageLifecycle;
import com.enableets.edu.pakage.core.source.PackageSource;

/**
 * 默认package的装饰器包装类
 * 等同于一个空的pkg
 */
public class DefaultEnablePackage extends AbstractEnablePackageDecorator implements IPackageLifecycle {

    //这里是模型定义
    private DefaultPackageWrapper packageWrapper;

    //这里是action行为定义
    private DefaultPackageLifecycle packageLifecycle;

    @Override
    public void pull(PackageSource source) {
        packageLifecycle.downLoad();
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
        return packageWrapper.getPackageFileInfo().getDownloadUrl();
    }
}
