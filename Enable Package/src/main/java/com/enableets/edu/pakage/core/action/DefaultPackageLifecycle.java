package com.enableets.edu.pakage.core.action;

import com.enableets.edu.pakage.core.bean.AbstractEnablePackage;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.source.PackageSource;

/**
 * 默认包的生命周期行为实现
 */
public class DefaultPackageLifecycle implements IPackageLifecycle {

    protected DefaultPackageLifecycle(){}

    public DefaultPackageLifecycle(AbstractEnablePackage enablePackage, Configuration configuration){
        this.downloadAction = new PackageDownloadAction(enablePackage, configuration);
        this.buildAction = new PackageBuildAction(enablePackage, configuration);
        this.parseAction = new PackageParseAction(enablePackage, configuration);
        this.destroyAction = new PackageDestroyAction(enablePackage, configuration);
    }

    private PackageDownloadAction downloadAction;

    private PackageBuildAction buildAction;

    private PackageParseAction parseAction;

    private PackageDestroyAction destroyAction;

    @Override
    public void pull(PackageSource source) {

    }

    @Override
    public void downLoad() {
        downloadAction.execute();
    }

    @Override
    public void parse() {
        parseAction.execute();
    }

    @Override
    public void build() {
        buildAction.execute();
    }

    @Override
    public void destroy() {
        destroyAction.execute();
    }
}
