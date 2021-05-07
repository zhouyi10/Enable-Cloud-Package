package com.enableets.edu.pakage.core.core.http;

import com.enableets.edu.pakage.core.core.Configuration;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/15
 **/
public abstract class AbstractHttpClient implements IHttpClient{

    public final Configuration configuration;

    public AbstractHttpClient(Configuration configuration){
        this.configuration = configuration;
    }
}
