package com.enableets.edu.pakage.framework.ppr.core.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.enableets.edu.framework.core.dao.datasource.CustomBaseDruidProperties;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/21
 **/
@Component
@ConfigurationProperties(prefix = "druid.paper.storage")
public class PaperStorageDruidProperties extends CustomBaseDruidProperties {
}
