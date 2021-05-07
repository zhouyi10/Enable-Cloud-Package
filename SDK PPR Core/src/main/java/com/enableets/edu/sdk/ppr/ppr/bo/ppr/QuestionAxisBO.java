package com.enableets.edu.sdk.ppr.ppr.bo.ppr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionAxisBO {

    private Long questionId;

    private String fileId;

    private Double xAxis;

    private Double yAxis;

    private Double width;

    private Double height;

    private Integer sequence;
}
