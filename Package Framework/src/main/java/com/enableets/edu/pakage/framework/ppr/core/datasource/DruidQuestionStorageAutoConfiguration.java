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
@MapperScan(basePackages = {"com.enableets.edu.pakage.framework.ppr.question.dao","com.enableets.edu.ppr.adapter.dao"}, sqlSessionFactoryRef = "questionStorageSqlSessionFactory")
public class DruidQuestionStorageAutoConfiguration {

    @Value(value = "${mybatis.mapper-locations}")
    private String mapperLocation;

    @Autowired
    private QuestionStorageDruidProperties questionStorageDruidProperties;

    @Bean(name = "questionStorageDataSource")
    public DataSource druidDataSource() {
        return new DataSourceCreator(questionStorageDruidProperties).create();
    }

    //其他数据源的事务管理器
    @Bean(name = "questionStorageTransactionManager")
    public DataSourceTransactionManager slaveTransactionManager() {
        return new DataSourceTransactionManager(druidDataSource());
    }

    @Bean(name = "questionStorageSqlSessionFactory")
    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("questionStorageDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocation));
        return sessionFactoryBean.getObject();
    }
}
