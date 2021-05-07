package com.enableets.edu.pakage.adapter;

import org.apache.commons.lang3.StringUtils;

import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.core.http.HttpClientFactory;
import com.enableets.edu.pakage.core.core.http.IHttpClient;

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
    public static PackageFileInfo get(String fileId, Configuration configuration){
        if (StringUtils.isBlank(fileId)) return null;
        IHttpClient httpClient = HttpClientFactory.getHttpClient(configuration);
        String fileStr = httpClient.doGet(new StringBuilder(configuration.getServerAddress()).append("/microservice/storageservice/v1/files/").append(fileId).toString());
        if (StringUtils.isBlank(fileStr)) return null;
        return fileStrToFileBO(fileStr);
    }

    public static String getDownloadUrl(String fileId, Configuration configuration){
        PackageFileInfo fileBO = get(fileId, configuration);
        if (fileBO == null) return null;
        return fileBO.getDownloadUrl();
    }

    /**
     * file storage file upload interface adapter
     * @param file
     * @return
     */
    public static PackageFileInfo upload(File file, Configuration configuration){
        if (!file.exists() || file.isDirectory()){
            throw new PackageInterfaceAdapterException("file is not exist!");
        }
        IHttpClient httpClient = HttpClientFactory.getHttpClient(configuration);
        String fileStr = httpClient.upload(new StringBuilder(configuration.getServerAddress()).append("/microservice/storageservice/v1/files/upload").toString(), file, file.getName());
        if (StringUtils.isBlank(fileStr)) return null;
        return fileStrToFileBO(fileStr);
    }

    private static PackageFileInfo fileStrToFileBO(String fileStr){
        if (StringUtils.isBlank(fileStr)) return null;
        PackageFileInfo fileBO = new PackageFileInfo();
        JSONObject json = new JSONObject(fileStr);
        if (json.get("status") == null || !json.get("status").toString().equals("success")){
            throw new PackageInterfaceAdapterException("File upload file storage error: status=" + json.get("status"));
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
            throw new PackageInterfaceAdapterException("JSON analysis response fileStr error!");
        }
        return fileBO;
    }

}
