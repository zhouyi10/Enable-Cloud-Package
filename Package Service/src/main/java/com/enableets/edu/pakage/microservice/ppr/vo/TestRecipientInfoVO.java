package com.enableets.edu.pakage.microservice.ppr.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Receive Student Information
 * @author walle_yu@enable-ets.com
 * @since 2018/5/31
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="TAsTestRecipientVO", description="Receive Student Information")
@Data
public class TestRecipientInfoVO {
	
	/** User ID*/
	@ApiModelProperty(value="User ID")
	private String userId;
	
	/** User Name*/
	@ApiModelProperty(value="User Name")
	private String userName;
	
	/** School ID*/
	@ApiModelProperty(value="School ID")
	private String schoolId;
	
	/** School Name*/
	@ApiModelProperty(value="School Name")
	private String schoolName;
	
	/** Term ID*/
	@ApiModelProperty(value="Term ID")
	private String termId;
	
	/** Term Name*/
	@ApiModelProperty(value="Term Name")
	private String termName;
	
	/** Grade Code*/
	@ApiModelProperty(value="Grade Code")
	private String gradeCode;
	
	/** Grade Name*/
	@ApiModelProperty(value="Grade Name")
	private String gradeName;
	
	/** Class ID*/
	@ApiModelProperty(value="Class ID")
	private String classId;
	
	/** Class Name*/
	@ApiModelProperty(value="Class Name")
	private String className;

	/** Group ID*/
	@ApiModelProperty(value = "Group ID")
	private String groupId;

	/** Group Name*/
	@ApiModelProperty(value = "Group Name")
	private String groupName;

}
