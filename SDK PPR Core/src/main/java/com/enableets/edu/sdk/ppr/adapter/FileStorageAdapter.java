package com.enableets.edu.sdk.ppr.adapter;

import com.enableets.edu.sdk.ppr.utils.StringUtils;
import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.http.HttpClientFactory;
import com.enableets.edu.sdk.ppr.http.IHttpClient;

import cn.hutool.json.JSONObject;
import java.io.File;
import java.util.ArrayList;

/**
 * File Storage Interface Adapter
 * @author walle_yu@enable-ets.com
 * @since 2020/06/22
 **/
public class FileStorageAdapter {

    /**
     *  file storage file get interface adapter
     * @param fileId
     * @return
     */
    public static FileBO get(String fileId, Configuration configuration){
        if (StringUtils.isBlank(fileId)) return null;
        IHttpClient httpClient = HttpClientFactory.getHttpClient(configuration);
        String fileStr = httpClient.doGet(new StringBuilder(configuration.getServerAddress()).append("/microservice/storageservice/v1/files/").append(fileId).toString());
        if (StringUtils.isBlank(fileStr)) return null;
        return fileStrToFileBO(fileStr);
    }

    public static String getDownloadUrl(String fileId, Configuration configuration){
        FileBO fileBO = get(fileId, configuration);
        if (fileBO == null) return null;
        return fileBO.getDownloadUrl();
    }

    /**
     * file storage file upload interface adapter
     * @param file
     * @return
     */
    public static FileBO upload(File file, Configuration configuration){
        if (!file.exists() || file.isDirectory()){
            throw new PPRInterfaceAdapterException("file is not exist!");
        }
        IHttpClient httpClient = HttpClientFactory.getHttpClient(configuration);
        String fileStr = httpClient.upload(new StringBuilder(configuration.getServerAddress()).append("/microservice/storageservice/v1/files/").append("/upload").toString(), file, file.getName());
        if (StringUtils.isBlank(fileStr)) return null;
        return fileStrToFileBO(fileStr);
    }

    private static FileBO fileStrToFileBO(String fileStr){
        if (StringUtils.isBlank(fileStr)) return null;
        FileBO fileBO = new FileBO();
        JSONObject json = new JSONObject(fileStr);
        if (json.get("status") == null || !json.get("status").toString().equals("success")){
            throw new PPRInterfaceAdapterException("File upload file storage error: status=" + json.get("status"));
        }
        try {
            JSONObject dataJson = new JSONObject(json.get("data"));
            fileBO.setFileId(dataJson.getStr("fileId"));
            fileBO.setMd5(dataJson.getStr("md5"));
            ArrayList list = dataJson.get("downloadUrls", ArrayList.class);
            fileBO.setDownloadUrl((String) list.get(0));
            fileBO.setName(dataJson.getStr("name"));
            fileBO.setSize(dataJson.getLong("size"));
            fileBO.setSizeDisplay(dataJson.getStr("sizeDisplay"));
            fileBO.setExt(dataJson.getStr("ext"));
        }catch (Exception e){
            throw new PPRInterfaceAdapterException("JSON analysis response fileStr error!");
        }
        return fileBO;
    }

}
