package com.enableets.edu.pakage.framework.ppr.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/23
 **/
@Data
public class PPRQuestionAxisBO {

    private String axisId;

    private String questionId;

    private String fileId;

    private Double xAxis;

    private Double yAxis;

    private Double width;

    private Double height;

    private Integer sequence;
}
