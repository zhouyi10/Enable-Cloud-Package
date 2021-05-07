package com.enableets.edu.sdk.ppr.ppr.core;

import com.enableets.edu.sdk.ppr.configuration.PPRSDKVersion;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/16
 **/
@Data
@XStreamAlias("header")
@AllArgsConstructor
public class Header {

    public Header() {

    }

    public Header(String encoding, String verification, String encryption, String compression, Property property) {
        this.version = PPRSDKVersion.get();
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
