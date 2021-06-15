package com.enableets.edu.pakage.core.core.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.enableets.edu.pakage.core.core.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/19
 **/
public class HttpClientImpl extends AbstractHttpClient {

    private static final int timeOut = 5000;

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private CloseableHttpClient httpClient;

    public HttpClientImpl(Configuration configuration) {
        super(configuration);
        this.httpClient = HttpClients.createDefault();
    }

    @Override
    public String doGet(String url) {
        String signUrl = SignatureUtils.sign(url, null, configuration);
        HttpGet httpGet = new HttpGet(signUrl);
        httpGet.setConfig(setConfig());
        return doHttp(httpGet);
    }

    @Override
    public String doPost(String url, String jsonStr) {
        String signUrl = SignatureUtils.sign(url, jsonStr, configuration);
        HttpPost httpPost = new HttpPost(signUrl);
        httpPost.setConfig(setConfig());
        httpPost.setHeader("Content-Type", CONTENT_TYPE);
        httpPost.setHeader("Accept", "application/json");
        StringEntity se = new StringEntity(jsonStr, StandardCharsets.UTF_8);
        se.setContentType("text/json");
        httpPost.setEntity(se);
        return doHttp(httpPost);

    }

    @Override
    public String upload(String url, File file, String filename) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(setConfig());
        FileBody bin = new FileBody(file);
        StringBody comment = new StringBody(filename, ContentType.TEXT_PLAIN);
        HttpEntity httpEntity = MultipartEntityBuilder.create().addPart("file", bin).addPart("filename", comment).build();
        httpPost.setEntity(httpEntity);
        return doHttp(httpPost);
    }

    private String doHttp(HttpUriRequest request){
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                return EntityUtils.toString(response.getEntity());
            }else{
                return null;
            }
        } catch (Exception e) {
            logger.error("HttpClient do request failure!", e);
            throw new packageHttpClientException("HttpClient do request failure!");
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private RequestConfig setConfig(){
        return RequestConfig.custom().setConnectTimeout(timeOut).setSocketTimeout(timeOut).setConnectionRequestTimeout(timeOut).build();
    }
}
