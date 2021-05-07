package com.enableets.edu.pakage.core.bean;

public abstract class AbstractEnablePackage<T extends Body> implements IEnablePackage<T> {
    private Header header;
    private T body;
    private Files files;
    private PackageFileInfo packageFileInfo;

    @Override
    public Header getHeader() {
        return header;
    }

    @Override
    public void setHeader(Header header){
        this.header = header;
    }

    @Override
    public T getBody() {
        return body;
    }

    @Override
    public void setBody(T body){
        this.body = body;
    }


    @Override
    public Files getFiles() {
        return files;
    }

    public void setFiles(Files files) {
        this.files = files;
    }

    @Override
    public PackageFileInfo getPackageFileInfo() {
        return packageFileInfo;
    }

    public void setPackageFileInfo(PackageFileInfo packageFileInfo){
        this.packageFileInfo = packageFileInfo;
    }
}
