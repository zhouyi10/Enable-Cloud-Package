package com.enableets.edu.sdk.ppr.http;

import com.enableets.edu.sdk.ppr.logging.Log;
import com.enableets.edu.sdk.ppr.logging.LogFactory;

import java.io.File;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/19
 **/
public interface IHttpClient {

    public static final Log logger = LogFactory.getLog(IHttpClient.class);

    public String doGet(String url);

    public String doPost(String url, String jsonStr);

    public String upload(String url, File file, String filename);

}
