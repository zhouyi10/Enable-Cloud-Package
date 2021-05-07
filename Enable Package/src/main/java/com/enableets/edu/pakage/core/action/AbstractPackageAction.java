package com.enableets.edu.pakage.core.action;

import com.enableets.edu.pakage.core.bean.AbstractEnablePackage;
import com.enableets.edu.pakage.core.core.Configuration;

/**
 * action 的抽象基类
 * 提供action操作相关上下文
 */
public abstract class AbstractPackageAction implements IPackageAction {

    private AbstractEnablePackage enablePackage;
    private Configuration configuration;

    public AbstractPackageAction(AbstractEnablePackage enablePackage, Configuration configuration){
        this.enablePackage = enablePackage;
        this.configuration = configuration;
    }

    public AbstractEnablePackage getEnablePackage() {
        return enablePackage;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
