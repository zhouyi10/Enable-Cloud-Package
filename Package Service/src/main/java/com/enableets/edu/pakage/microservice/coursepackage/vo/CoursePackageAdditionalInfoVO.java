package com.enableets.edu.pakage.microservice.coursepackage.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: CoursePackageAdditionalInfoVO
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="CoursePackageAdditionalInfoVO", description="CoursePackageAdditionalInfoVO")
public class CoursePackageAdditionalInfoVO extends AddCoursePackageAdditionalInfoVO {

	@ApiModelProperty(value = "Resource ID")
	private String contentId;

	@ApiModelProperty(value = "Group information")
	private List<GroupVO> groups;
}
