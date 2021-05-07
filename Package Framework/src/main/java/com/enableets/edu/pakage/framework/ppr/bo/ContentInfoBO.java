package com.enableets.edu.pakage.framework.ppr.bo;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 *
 */
@Data
public class ContentInfoBO implements Serializable {

	private static final long serialVersionUID = -1721100885883732287L;

	private String contentId;

	// Resource Name
	private String contentName;

	// Resource description
	private String contentDescription;

	// Original Resource Id
	private String providerContentId;

	// Source code
	private String providerCode;

	// Resource Type Code
	private String typeCode;
	
	// Resource Type Name
	private String typeName;

	// Resource uploader
	private String creator;

	// Resource uploader name
	private String creatorName;

	// Resource Create time
	private String createTime;

	// Resource Update time
	private String updateTime;

	// School ID
	private String schoolId;

	// School Name
	private String schoolName;

	// Stage Code
	private String stageCode;

	// Stage Name
	private String stageName;

	// Grade Code
	private String gradeCode;

	// Grade Name
	private String gradeName;

	// Subject Code
	private String subjectCode;

	// Subject Name
	private String subjectName;

	// Content Description
	private String htmlContext;

	// No html content
	private String plaintextContext;

	// Resource Browse Number
	private Integer browseNumber;

	// Resource Download Number
	private Integer downloadNumber;

	// Resource Praise Number
	private Integer praiseNumber;

	//** Resource File*//
	private List<ContentFileInfoBO> fileList;

}
