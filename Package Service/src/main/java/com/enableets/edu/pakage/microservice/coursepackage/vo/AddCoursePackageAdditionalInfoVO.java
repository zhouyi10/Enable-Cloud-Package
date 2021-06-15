package com.enableets.edu.pakage.microservice.coursepackage.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: CoursePackageAdditionalInfoVO
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="AddCoursePackageAdditionalInfoVO", description="AddCoursePackageAdditionalInfoVO")
public class AddCoursePackageAdditionalInfoVO {

	@ApiModelProperty(value = "Required hours")
	private Integer requireClassHour;

	@ApiModelProperty(value = "teaching objectives")
	private String teachingGoal;

	@ApiModelProperty(value = "Difficulties in teaching")
	private String teachingImportantPoints;

	@ApiModelProperty(value = "Academic analysis")
	private String learnerAnalysis;

	@ApiModelProperty(value = "Teaching environment")
	private String teachingEnvironment;
}
