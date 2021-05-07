package com.enableets.edu.pakage.core.bean;

/**
 * 描述了package的基本属性
 */
public interface IEnablePackage<T extends Body> {
    Header getHeader();
    void setHeader(Header header);
    T getBody();
    void setBody(T body);
    Files getFiles();
    void setFiles(Files files);
    PackageFileInfo getPackageFileInfo();
    void setPackageFileInfo(PackageFileInfo packageFileInfo);
}
