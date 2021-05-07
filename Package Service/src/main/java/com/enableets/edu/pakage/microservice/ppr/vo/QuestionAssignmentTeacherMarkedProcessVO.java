package com.enableets.edu.pakage.microservice.ppr.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Teacher review progress
 * @author walle_yu@enable-ets.com
 * @since 2020/09/24
 **/
@Data
@ApiModel(value = "QuestionAssignmentTeacherMarkedProcessVO", description = "")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionAssignmentTeacherMarkedProcessVO {

    @ApiModelProperty(value = "User ID")
    private String userId;

    @ApiModelProperty(value = "User Name")
    private String fullName;

    @ApiModelProperty(value = "Number of people reviewed")
    private Integer markedCount;

    @ApiModelProperty(value = "Reviewed Process")
    private Float markedRatio;

    @ApiModelProperty(value = "Total people")
    private Integer total;

}
