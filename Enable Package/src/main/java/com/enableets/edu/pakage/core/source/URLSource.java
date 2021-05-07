package com.enableets.edu.pakage.core.source;

public class URLSource extends PackageSource {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getId() {
        return url;
    }

    public URLSource(String url) {
        this.url = url;
    }
}
