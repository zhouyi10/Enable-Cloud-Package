package com.enableets.edu.pakage.core.action;

import com.enableets.edu.pakage.core.bean.AbstractEnablePackage;
import com.enableets.edu.pakage.core.core.Configuration;

/**
 * 提供解析行为实现
 */
public class PackageParseAction extends AbstractPackageAction {

    public PackageParseAction(AbstractEnablePackage enablePackage, Configuration configuration) {
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
