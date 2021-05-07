package com.enableets.edu.pakage.manager.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/21
 **/
@Data
public class QueryContentBO {

    /** 资源来源*/
    private String providerCode;

    /** 学段编码*/
    private String stageCode;

    /** 年级编码*/
    private String gradeCode;

    /** 学科编码*/
    private String subjectCode;

    /** 资源类型编码*/
    private String typeCode;

    /** 教材版本编码*/
    private String materialVersion;

    /** */
    private String searchCode;

    /** */
    private String keyword;

    /** */
    private String userId;

    /** */
    private Integer offset;

    /** */
    private Integer rows;
}
