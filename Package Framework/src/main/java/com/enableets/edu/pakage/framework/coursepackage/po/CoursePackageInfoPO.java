package com.enableets.edu.pakage.framework.coursepackage.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.enableets.edu.framework.core.dao.BasePO;

import lombok.Data;

/**
 * @Description: CoursePackageInfoPO
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-24
 */
@Data
@Entity
@Table(name = "course_package_info")
public class CoursePackageInfoPO extends BasePO {

	@Id
	@Column(name = "content_id")
	private String contentId;

	@Column(name = "require_class_hour")
	private Integer requireClassHour;

	@Column(name = "teaching_goal")
	private String teachingGoal;

	@Column(name = "teaching_important_points")
	private String teachingImportantPoints;
	@Column(name = "learner_analysis")
	private String learnerAnalysis;

	@Column(name = "teaching_environment")
	private String teachingEnvironment;

	@Column(name = "status")
	private String status;

}
