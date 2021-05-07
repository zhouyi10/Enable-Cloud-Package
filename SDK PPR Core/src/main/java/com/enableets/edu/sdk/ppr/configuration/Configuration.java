package com.enableets.edu.sdk.ppr.configuration;

import com.enableets.edu.sdk.ppr.annotation.NotNull;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/22
 **/
public class Configuration {

    @NotNull
    private String serverAddress;

    @NotNull
    private String PPRTempPath;

    @NotNull
    private String clientId;

    @NotNull
    private String clientSecret;

    public String getServerAddress() {
        return serverAddress;
    }

    public Configuration setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        return this;
    }

    public String getPPRTempPath() {
        return PPRTempPath;
    }

    public Configuration setPPRTempPath(String PPRTempPath) {
        this.PPRTempPath = PPRTempPath;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public Configuration setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Configuration setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }
}
