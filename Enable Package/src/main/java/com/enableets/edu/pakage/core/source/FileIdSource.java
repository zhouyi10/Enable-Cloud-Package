package com.enableets.edu.pakage.core.source;

public class FileIdSource extends PackageSource {

    private String fileId;

    @Override
    public String getId() {
        return fileId;
    }

    public FileIdSource(String fileId) {
        this.fileId = fileId;
    }
}
