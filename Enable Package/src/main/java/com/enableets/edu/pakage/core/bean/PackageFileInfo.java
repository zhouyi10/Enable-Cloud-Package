package com.enableets.edu.pakage.core.bean;

import org.apache.commons.lang3.StringUtils;

import com.enableets.edu.pakage.core.source.PackageSource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageFileInfo extends PackageSource {

    private String fileId;

    private String downloadUrl;

    private String md5;

    private String name;

    private Long size;

    private String sizeDisplay;

    private String ext;

    private String localZipPath;


    @Override
    public String getId() {
        if (StringUtils.isNotEmpty(fileId)) return fileId;
        if (StringUtils.isNotBlank(downloadUrl)) return downloadUrl;
        if (StringUtils.isNotBlank(localZipPath)) return localZipPath;
        return null;
    }
}
