package com.enableets.edu.pakage.framework.ppr.core.tablecopy;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.enableets.edu.pakage.framework.ppr.core.datasource.DruidPaperStorageAutoConfiguration;
import com.enableets.edu.pakage.framework.ppr.core.datasource.DruidQuestionStorageAutoConfiguration;

/**
 * Data Jdbc Copy Service
 */
@Configuration
@AutoConfigureAfter({DruidPaperStorageAutoConfiguration.class, DruidPaperStorageAutoConfiguration.class, DruidQuestionStorageAutoConfiguration.class})
public class TableDataCopyConfig {

    /**
     * copy data to target from origin
     * @param originJdbcTemplate
     * @param targetJdbcTemplate
     * @return
     */
    @Bean("questionCopyService")
    public TableCopyComponent questionCopyService(@Qualifier("questionStorageJdbcTemplate") JdbcTemplate originJdbcTemplate, @Qualifier("paperStorageJdbcTemplate") JdbcTemplate targetJdbcTemplate){
        return new TableCopyComponent(originJdbcTemplate, targetJdbcTemplate, "/copy/CopyQuestionInfoMapper.xml");
    }

    @Bean("paperCopyService")
    public TableCopyComponent paperCopyService(@Qualifier("paperStorageJdbcTemplate") JdbcTemplate originJdbcTemplate, @Qualifier("pprJdbcTemplate") JdbcTemplate targetJdbcTemplate){
        return new TableCopyComponent(originJdbcTemplate, targetJdbcTemplate, "/copy/CopyExamInfoMapper.xml");
    }

    @Bean("paperStorageJdbcTemplate")
    public JdbcTemplate paperStorageJdbcTemplate(@Qualifier("paperStorageDataSource") DataSource datSource){
        return new JdbcTemplate(datSource);
    }

    /**
     * @param datSource
     * @return
     */
    @Bean("questionStorageJdbcTemplate")
    public JdbcTemplate questionStorageJdbcTemplate(@Qualifier("questionStorageDataSource") DataSource datSource){
        return new JdbcTemplate( datSource);
    }

    @Bean("pprJdbcTemplate")
    public JdbcTemplate paperJdbcTemplate(@Qualifier("packageDataSource") DataSource datSource){
        return new JdbcTemplate(datSource);
    }
}
