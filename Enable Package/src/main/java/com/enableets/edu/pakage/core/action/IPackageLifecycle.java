package com.enableets.edu.pakage.core.action;

import com.enableets.edu.pakage.core.source.PackageSource;

/**
 * 描述package基本生命周期行为
 */
public interface IPackageLifecycle {
    void pull(PackageSource source);
    void downLoad();
    void parse();
    void build();
    void destroy();
}
