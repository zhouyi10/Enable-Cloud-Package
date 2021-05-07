package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Test recipient VO
 * @author walle_yu@enable-ets.com
 * @since 2020/8/21
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TestRecipientInfoDTO {
	
	/** User ID*/
	private String userId;
	
	/** User Name*/
	private String userName;
	
	/** School ID*/
	private String schoolId;
	
	/** School Name*/
	private String schoolName;
	
	/** Term ID*/
	private String termId;
	
	/** Term Name*/
	private String termName;
	
	/** Grade Code*/
	private String gradeCode;
	
	/** Grade Name*/
	private String gradeName;
	
	/** Class ID*/
	private String classId;
	
	/** Class Name*/
	private String className;

	/** Group ID*/
	private String groupId;

	/** Group Name*/
	private String groupName;

}
