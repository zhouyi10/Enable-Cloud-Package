package com.enableets.edu.sdk.ppr.adapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/16
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StepInfoBO {

    private String activityId;

    private String stepId;
}
