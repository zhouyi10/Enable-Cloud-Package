package com.enableets.edu.pakage.framework.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/25
 **/
@Data
public class QuestionAxisBO {

    private Long questionId;

    private String fileId;

    private Double xAxis;

    private Double yAxis;

    private Double width;

    private Double height;

    private Integer sequence;
}
