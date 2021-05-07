package com.enableets.edu.pakage.core.core;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/22
 **/
public class Configuration {

    private String serverAddress;

    private String PPRTempPath;

    private String clientId;

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
