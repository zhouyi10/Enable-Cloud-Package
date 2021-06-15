package com.enableets.edu.pakage.microservice.coursepackage.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: ContentFileInfoVO
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "ContentFileInfoVO", description = "ContentFileInfoVO")
public class ContentFileInfoVO extends AddContentFileInfoVO {

	@ApiModelProperty(value = "Resource ID")
	private String contentId;
}
