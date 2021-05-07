package com.enableets.edu.pakage.core.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@XStreamAlias("header")
public class Header {

    public Header() {

    }

    public Header(String version, String encoding, String verification, String encryption, String compression, Property property) {
        this.version = version;
        this.encoding = encoding;
        this.verification = verification;
        this.encryption = encryption;
        this.compression = compression;
        this.property = property;
    }

    private String version;
    private String encoding;
    private String verification;
    private String encryption;
    private String compression;
    private Property property;
}
