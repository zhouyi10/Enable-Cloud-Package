package com.enableets.edu.pakage.framework.coursepackage.bo;

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
public class CoursePackageAdditionalInfoBO {

	/**
	 * require classHour
	 */
	
	private Integer requireClassHour;

	/**
	 * teaching goal
	 */
	
	private String teachingGoal;

	/**
	 * Difficulties in teaching
	 */
	
	private String teachingImportantPoints;

	/**
	 * Academic analysis
	 */
	
	private String learnerAnalysis;

	/**
	 * Teaching environment
	 */
	
	private String teachingEnvironment;

	/**
	 * Group information
	 */
	private List<GroupBO> groups;

}
