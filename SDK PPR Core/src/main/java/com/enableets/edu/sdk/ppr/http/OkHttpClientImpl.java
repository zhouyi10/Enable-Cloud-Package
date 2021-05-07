package com.enableets.edu.sdk.ppr.http;

import com.enableets.edu.sdk.ppr.configuration.Configuration;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/19
 **/
public class OkHttpClientImpl extends AbstractHttpClient {

    private static final Long timeOut = 3000L;

    private static final MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");

    private OkHttpClient okHttpClient = null;

    public OkHttpClientImpl(Configuration configuration){
        super(configuration);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public String doGet(String url){
        String signUrl = SignatureUtils.sign(url, null, configuration);
        Request request = new Request.Builder().url(signUrl).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            logger.error("OkHttpClient doGet request failure!", e);
            throw new PPRHttpClientException("OkHttpClient doGet request failure!");
        }
    }

    @Override
    public String doPost(String url, String jsonStr){
        String signUrl = SignatureUtils.sign(url, jsonStr, configuration);
        RequestBody requestBody = RequestBody.create(mediaType, jsonStr);
        return doHttp(signUrl, requestBody);
    }

    @Override
    public String upload(String url, File file, String filename) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", filename, RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();

        return doHttp(url, requestBody);
    }

    private String doHttp(String url, RequestBody body){
        Request request = new Request.Builder().post(body).url(url).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            logger.error("OkHttpClient doPost request failure!", e);
            throw new PPRHttpClientException("OkHttpClient doPost request failure!");
        }
        if (response.isSuccessful()) return response.body().toString();
        else{
            throw new PPRHttpClientException("The interface["+url+"] error!");
        }
    }
}
