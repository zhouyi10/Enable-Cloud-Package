package com.enableets.edu.pakage.microservice.coursepackage.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Description: AddCoursePackageVO
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "KnowledgeInfoVO", description = "KnowledgeInfoVO")
public class KnowledgeInfoVO {


	 @ApiModelProperty(value = "knowledge id")
	 private String knowledgeId;
	 

	 @ApiModelProperty(value = "knowledge name")
	 private String knowledgeName;

	 @ApiModelProperty(value = "search code")
     private String searchCode;

	 @ApiModelProperty(value = "Version")
	 private String materialVersion;
	 
	 
	 @ApiModelProperty(value = "Version name")
	 private String materialVersionName;

}
