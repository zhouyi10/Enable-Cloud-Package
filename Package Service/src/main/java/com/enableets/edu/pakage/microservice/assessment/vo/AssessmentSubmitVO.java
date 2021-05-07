package com.enableets.edu.pakage.microservice.assessment.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/25 10:49
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel(value = "AssessmentSubmitVO", description = "Assessment Submit VO")
public class AssessmentSubmitVO {

    @ApiParam(value = "process Instance Id", required = true)
    private String processInstanceId;

    @ApiParam(value = "user Id", required = true)
    private String userId;
}
