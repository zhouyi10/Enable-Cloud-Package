package com.enableets.edu.pakage.framework.ppr.core.datasource;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/21
 **/
@Configuration
@MapperScan(basePackages = {"com.enableets.edu.pakage.framework.ppr.test","com.enableets.edu.pakage.framework.coursepackage.dao"}, sqlSessionFactoryRef = "packageSqlSessionFactory")
public class DruidPackageMicroServiceAutoConfiguration {

    @Value(value = "${mybatis.mapper-locations}")
    private String mapperLocation;

    @Autowired
    private PackageDruidProperties properties;

    @Bean(name = "packageDataSource")
    public DataSource druidDataSource() {
        return new DataSourceCreator(properties).create();
    }

    @Bean(name = "packageTransactionManager")
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("packageDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "packageSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("packageDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocation));
        return sessionFactoryBean.getObject();
    }

}
