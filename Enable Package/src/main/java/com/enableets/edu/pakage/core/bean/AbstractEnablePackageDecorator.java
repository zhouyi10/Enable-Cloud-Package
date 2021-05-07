package com.enableets.edu.pakage.core.bean;

import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.source.PackageSource;

public abstract class AbstractEnablePackageDecorator<T extends Body> extends AbstractEnablePackage<T>{
    private Configuration configuration;
    private PackageSource packageSource;
    public abstract String getId();

    public void setConfiguration(){
        this.configuration = configuration;
    }

    public Configuration getConfiguration(){
        return configuration;
    }

    public PackageSource getPackageSource() {
        return packageSource;
    }

    public void setPackageSource(PackageSource packageSource) {
        this.packageSource = packageSource;
    }
}
