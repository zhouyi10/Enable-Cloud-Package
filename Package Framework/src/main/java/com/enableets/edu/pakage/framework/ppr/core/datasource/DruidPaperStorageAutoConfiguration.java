package com.enableets.edu.pakage.framework.ppr.core.datasource;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/09
 **/
@Configuration
@MapperScan(basePackages = {"com.enableets.edu.pakage.framework.ppr.paper.dao"}, sqlSessionFactoryRef = "paperStorageSqlSessionFactory")
public class DruidPaperStorageAutoConfiguration {

    @Value(value = "${mybatis.mapper-locations}")
    private String mapperLocation;

    @Autowired
    private PaperStorageDruidProperties paperStorageDruidProperties;

    @Bean(name = "paperStorageDataSource")
    public DataSource druidDataSource() {
        return new DataSourceCreator(paperStorageDruidProperties).create();
    }

    //其他数据源的事务管理器
    @Bean(name = "paperStorageTransactionManager")
    public DataSourceTransactionManager slaveTransactionManager() {
        return new DataSourceTransactionManager(druidDataSource());
    }

    @Bean(name = "paperStorageSqlSessionFactory")
    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("paperStorageDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocation));
        return sessionFactoryBean.getObject();
    }
}
