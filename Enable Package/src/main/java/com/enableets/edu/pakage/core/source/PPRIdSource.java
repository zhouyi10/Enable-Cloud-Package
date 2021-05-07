package com.enableets.edu.pakage.core.source;

public class PPRIdSource extends PackageSource {

    private String id;

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PPRIdSource(String id) {
        this.id = id;
    }
}
