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

	@Column(name = "stage_code")
	private String stageCode;

	@Column(name = "stage_name")
	private String stageName;

	@Column(name = "grade_code")
	private String gradeCode;

	@Column(name = "grade_name")
	private String gradeName;

	@Column(name = "subject_code")
	private String subjectCode;

	@Column(name = "subject_name")
	private String subjectName;

	@Column(name = "material_version")
	private String materialVersion;

	@Column(name = "material_version_name")
	private String materialVersionName;

	@Column(name = "knowledge_id")
	private String knowledgeId;

	@Column(name = "knowledge_name")
	private String knowledgeName;

	@Column(name = "search_code")
	private String searchCode;

	@Column(name = "type_code")
	private String typeCode;

	@Column(name = "type_name")
	private String typeName;

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
