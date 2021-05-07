package com.enableets.edu.pakage.core.factory;

import com.enableets.edu.pakage.core.bean.AbstractEnablePackageDecorator;
import com.enableets.edu.pakage.core.bean.DefaultEnablePackage;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.source.PackageSource;

/**
 * 默认的pkg实现
 * 等同一个空的包
 */
public class DefaultEnablePackageFactory extends AbstractEnablePackageFactory<DefaultEnablePackage> {

    public DefaultEnablePackageFactory(Configuration configuration) {
        super(configuration);
    }

    @Override
    protected AbstractEnablePackageDecorator fetchPackage(PackageSource source) {
        DefaultEnablePackage pkg = new DefaultEnablePackage();
        pkg.downLoad();
        pkg.parse();
        return pkg;
    }
}
