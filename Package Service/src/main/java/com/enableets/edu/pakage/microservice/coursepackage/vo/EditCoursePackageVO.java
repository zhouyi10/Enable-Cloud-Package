package com.enableets.edu.pakage.microservice.coursepackage.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Description: AddCoursePackageVO
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "EditCoursePackageVO", description = "EditCoursePackageVO")
public class EditCoursePackageVO extends AddCoursePackageVO {

}
