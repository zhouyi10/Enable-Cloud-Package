package com.enableets.edu.pakage.manager.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Package System Configuration Reader
 * @author walle_yu@enable-ets.com
 * @since 2020/09/25
 **/
@Component
@Data
public class PackageConfigReader {

    @Value("${system-identifier.worker-id:1}")
    private Long workerId;

    @Value("${system-identifier.datacenter-id:99}")
    private Long dataCenterId;

    @Value("${security.oauth2.client.clientId:wiedu_application_key}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    @Value("${storage.host.upload-url}")
    private String uploadFileUrl;

    @Value("${manager.onlinefile.url}")
    private String onlineFileUrl;

    @Value("${package.config.ppr.dir.temp}")
    private String pprMakeTempDir;

    @Value("${manager.content.url:/manager/content}")
    private String contentManagerUrl;

    @Value("${nginx.server-address}")
    private String nginxServer;
}