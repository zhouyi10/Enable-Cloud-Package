package com.enableets.edu.pakage.core.factory;

import com.enableets.edu.pakage.core.bean.AbstractEnablePackageDecorator;
import com.enableets.edu.pakage.core.source.PackageSource;

public interface IEnablePackageFactory {
    public AbstractEnablePackageDecorator getPackage(PackageSource source);
}
