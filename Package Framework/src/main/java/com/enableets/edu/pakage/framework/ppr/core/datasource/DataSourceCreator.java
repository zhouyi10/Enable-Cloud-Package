package com.enableets.edu.pakage.framework.ppr.core.datasource;

import javax.sql.DataSource;

import com.enableets.edu.framework.core.dao.datasource.CustomBaseDruidProperties;
import com.enableets.edu.framework.core.dao.datasource.CustomDruidAutoConfiguration;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/09
 **/
public class DataSourceCreator {

    private CustomBaseDruidProperties properties;

    public DataSourceCreator(CustomBaseDruidProperties properties) {
        this.properties = properties;
    }

    public DataSource create(){
        CustomDruidAutoConfiguration dataSource = new CustomDruidAutoConfiguration();
        return dataSource.dataSource(properties);
    }

}
