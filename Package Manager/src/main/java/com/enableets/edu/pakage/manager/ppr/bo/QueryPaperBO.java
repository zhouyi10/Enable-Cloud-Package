package com.enableets.edu.pakage.manager.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/11
 **/
@Data
public class QueryPaperBO {

    private String stageCode;

    private String gradeCode;

    private String subjectCode;

    private String providerCode;

    private String materialVersion;

    private String searchCode;

    private String name;

    private String userId;

    private Integer offset;

    private Integer rows;
}
