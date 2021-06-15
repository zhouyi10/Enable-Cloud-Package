package com.enableets.edu.pakage.microservice.coursepackage.vo;

import java.util.List;

import com.enableets.edu.module.service.core.BaseVO;
import com.enableets.edu.module.service.core.MicroServiceException;
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
@ApiModel(value = "AddCoursePackageVO", description = "AddCoursePackageVO")
public class AddCoursePackageVO extends BaseVO {

	@ApiModelProperty(value = "Resource ID")
	private String contentId;


	@ApiModelProperty(value = "Resource Name")
	private String contentName;


	@ApiModelProperty(value = "Source code")
	private String providerCode;

	@ApiModelProperty(value = "Category code")
	private String typeCode;


	@ApiModelProperty(value = "classification name")
	private String typeName;


	@ApiModelProperty(value = "Resource description")
	private String contentDescription;


	@ApiModelProperty(value = "School id")
	private String schoolId;


	@ApiModelProperty(value = "School Name")
	private String schoolName;


	@ApiModelProperty(value = "School coding")
	private String stageCode;


	@ApiModelProperty(value = "School section name")
	private String stageName;

	@ApiModelProperty(value = "Grade code")
	private String gradeCode;


	@ApiModelProperty(value = "Grade name")
	private String gradeName;

	@ApiModelProperty(value = "Subject code")
	private String subjectCode;


	@ApiModelProperty(value = "Subject name")
	private String subjectName;


	@ApiModelProperty(value = "Knowledge list")
	private List<KnowledgeInfoVO> knowledgeList;

	@ApiModelProperty(value = "Attachment list")
	private List<AddContentFileInfoVO> fileList;


	@ApiModelProperty(value = "Additional information about the course package")
	private AddCoursePackageAdditionalInfoVO coursePackage;


	@ApiModelProperty(value = "Resource uploader", required = true)
	private String creator;


	@ApiModelProperty(value = "Resource uploader name")
	private String creatorName;


	@Override
	public void validate() throws MicroServiceException {
		/*this.notBlank(contentName, "contentName");
		this.notBlank(typeCode, "typeCode");
		this.notBlank(creator, "creator");
		this.notBlank(creatorName, "creatorName");
		this.notNull(knowledgeList, "knowledgeList");
		this.notNull(fileList, "fileList");
		this.notNull(coursePackage, "coursePackage");*/
	}
}
