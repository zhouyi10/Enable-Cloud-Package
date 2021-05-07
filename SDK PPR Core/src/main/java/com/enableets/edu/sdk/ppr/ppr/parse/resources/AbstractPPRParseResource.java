package com.enableets.edu.sdk.ppr.ppr.parse.resources;

import com.enableets.edu.sdk.ppr.configuration.Configuration;

/**
 * @Date 2020/07/01 16:52
 * @Author caleb_liu@enable-ets.com
 **/

public abstract class AbstractPPRParseResource implements IPPRResource {

    protected Configuration configuration;

    public boolean ignoreVersion = Boolean.FALSE;

    public String pprRootPath;

    public IPPRResource setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public void setIgnoreVersion(Boolean ignoreVersion){
        if (ignoreVersion != null)
            this.ignoreVersion = ignoreVersion;
    }

    @Override
    public String getPPRRootPath() {
        return this.pprRootPath;
    };

}
