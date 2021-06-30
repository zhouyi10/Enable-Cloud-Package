package com.enableets.edu.pakage.framework.coursepackage.bo;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @Description: CoursePackageBO
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-24
 */
@Data
public class CoursePackageBO {

	private String contentId;

	/**
	 * Resource Name
	 */
	private String contentName;

	/**
	 * Source code
	 */
	private String providerCode;

	/**
	 * Category code
	 */
	private String typeCode;

	/**
	 * classification name
	 */
	private String typeName;

	/**
	 * Resource description
	 */
	private String contentDescription;

	/**
	 * School id
	 */
	private String schoolId;

	/**
	 * School Name
	 */
	private String schoolName;

	/**
	 * School coding
	 */
	private String stageCode;

	/**
	 * School section name
	 */
	private String stageName;

	/**
	 * Grade code
	 */
	private String gradeCode;

	/**
	 * Grade name
	 */
	private String gradeName;

	/**
	 * Subject code
	 */
	private String subjectCode;

	/**
	 * Subject name
	 */
	private String subjectName;

	/**
	 * version
	 */
	private String materialVersion;

	/**
	 * search Code
	 */
	private String searchCode;

	/**
	 * keyword
	 */
	private String keyword;

	/**
	 * order Type
	 */
	private String orderType;

	/**
	 * start Time
	 */
	private String startTime;

	/**
	 * end Time
	 */
	private String endTime;

	/**
	 * offset
	 */
	private Integer offset;

	/**
	 * row
	 */
	private Integer rows;

	/**
	 * Knowledge list
	 */
	private List<KnowledgeInfoBO> knowledgeList;

	/**
	 * Attachment list
	 */
	private List<ContentFileInfoBO> fileList;


	private CoursePackageAdditionalInfoBO
			coursePackage;

	/**
	 * Resource uploader id
	 */
	private String creator;

	/**
	 * Resource uploader
	 */
	private String creatorName;

	/**
	 * create Time
	 */
	private String createTime;

	private List<CoursePackagePlanInfoBO> plans;

	private List<String> typeCodes;
}
