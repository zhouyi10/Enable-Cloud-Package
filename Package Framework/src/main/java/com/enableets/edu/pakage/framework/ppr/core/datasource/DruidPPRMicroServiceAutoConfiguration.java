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
@MapperScan(basePackages = {"com.enableets.edu.pakage.framework.ppr.test"}, sqlSessionFactoryRef = "pprSqlSessionFactory")
public class DruidPPRMicroServiceAutoConfiguration {

    @Value(value = "${mybatis.mapper-locations}")
    private String mapperLocation;

    @Autowired
    private PPRDruidProperties properties;

    @Bean(name = "pprDataSource")
    public DataSource druidDataSource() {
        return new DataSourceCreator(properties).create();
    }

    @Bean(name = "pprTransactionManager")
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("pprDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "pprSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("pprDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocation));
        return sessionFactoryBean.getObject();
    }

}
