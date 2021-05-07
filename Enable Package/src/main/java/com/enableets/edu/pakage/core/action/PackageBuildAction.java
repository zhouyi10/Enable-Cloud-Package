package com.enableets.edu.pakage.core.action;

import com.enableets.edu.pakage.core.bean.AbstractEnablePackage;
import com.enableets.edu.pakage.core.core.Configuration;

/**
 * 提供打包行为的实现
 */
public class PackageBuildAction extends AbstractPackageAction {

    public PackageBuildAction(AbstractEnablePackage enablePackage, Configuration configuration) {
        super(enablePackage, configuration);
    }

    @Override
    public String getActionName() {
        return null;
    }

    @Override
    public Object execute() {

        return null;
    }
}
