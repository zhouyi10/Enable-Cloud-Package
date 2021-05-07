package com.enableets.edu.sdk.ppr.configuration;

import com.enableets.edu.sdk.ppr.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/15
 **/
public class PPRSDKVersion {

    private static final String PPR_VERSION_CONF_DOC = "prop.properties";

    private static final String PPR_VERSION_CONF_KEY = "version";

    private static String version;

    public static String get(){
        if (StringUtils.isBlank(version)) read();
        return version;
    }

    private static void read(){
        InputStream is = PPRSDKVersion.class.getClassLoader().getResourceAsStream(PPR_VERSION_CONF_DOC);
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException e) {
            throw new PPRConfigurationInitException("Missing configuration file \""+PPR_VERSION_CONF_DOC+"\" ");
        }
        version = props.getProperty(PPR_VERSION_CONF_KEY);
        if (StringUtils.isBlank(version)){
            throw new PPRConfigurationInitException("Missing property \"version\"");
        }
    }
}
