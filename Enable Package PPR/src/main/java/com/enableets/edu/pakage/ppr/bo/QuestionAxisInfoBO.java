package com.enableets.edu.pakage.ppr.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/16
 **/
@Data
@AllArgsConstructor
public class QuestionAxisInfoBO {

    public QuestionAxisInfoBO(){}

    private Long questionId;

    private String fileId;

    private Double xAxis;

    private Double yAxis;

    private Double width;

    private Double height;

    private String url;

    private Integer sequence;
}
