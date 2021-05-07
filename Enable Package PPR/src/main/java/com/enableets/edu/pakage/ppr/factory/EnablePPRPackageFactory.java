package com.enableets.edu.pakage.ppr.factory;

import com.enableets.edu.pakage.core.bean.AbstractEnablePackageDecorator;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.factory.AbstractEnablePackageFactory;
import com.enableets.edu.pakage.core.source.PackageSource;
import com.enableets.edu.pakage.ppr.bean.EnablePPR;

/**
 * PPR package的实现工厂
 */
public class EnablePPRPackageFactory extends AbstractEnablePackageFactory<EnablePPR> {


    public EnablePPRPackageFactory(Configuration configuration) {
        super(configuration);
    }

    @Override
    protected AbstractEnablePackageDecorator fetchPackage(PackageSource source) {
        EnablePPR enablePPR = new EnablePPR(this.getConfiguration());
        enablePPR.pull(source);
        enablePPR.parse();
        return enablePPR;
    }
}
