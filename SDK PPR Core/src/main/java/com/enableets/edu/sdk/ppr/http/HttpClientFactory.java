package com.enableets.edu.sdk.ppr.http;

import com.enableets.edu.sdk.ppr.configuration.Configuration;

import java.lang.reflect.Constructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/19
 **/
public class HttpClientFactory {

    private static Constructor<? extends IHttpClient> httpClientConstructor;

    private HttpClientFactory(){}

    //Auto scan HTTP request implementation, loading priority is: HttpClient -> OkHttpClient
    static {
        tryImplementation(HttpClientFactory::useHttpClient);
        tryImplementation(HttpClientFactory::useOkHttpClient);
    }

    public static IHttpClient getHttpClient(Configuration configuration){
        try{
            return httpClientConstructor.newInstance(configuration);
        }catch (Throwable t){
            throw new PPRHttpClientException("Error creating HttpClient, Cause: " + t, t);
        }
    }

    private static synchronized void useHttpClient(){
        setImplementation(com.enableets.edu.sdk.ppr.http.HttpClientImpl.class);
    }

    private static synchronized void useOkHttpClient(){
        setImplementation(com.enableets.edu.sdk.ppr.http.OkHttpClientImpl.class);
    }

    private static void tryImplementation(Runnable runnable) {
        if (httpClientConstructor == null) {
            try {
                runnable.run();
            } catch (Throwable t) {
                // ignore
            }
        }
    }

    private static void setImplementation(Class<? extends IHttpClient> implClass){
        try {
            Constructor<? extends IHttpClient> candidate = implClass.getConstructor(Configuration.class);
            IHttpClient httpClient = candidate.newInstance(new Configuration());
            httpClientConstructor = candidate;
        } catch (Throwable t){
            throw new PPRHttpClientException("Error setting HttpClient implementation.  Cause: " + t, t);
        }

    }

}
