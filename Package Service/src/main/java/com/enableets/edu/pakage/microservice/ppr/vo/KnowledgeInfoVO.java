package com.enableets.edu.pakage.microservice.ppr.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Knowledge Info
 * @author walle_yu
 * @since 2020/10/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "knowledgeInfoVO", description = "Knowledge Info")
public class KnowledgeInfoVO {
	
	/** Knowledge Id  */
	@ApiModelProperty(value = "Knowledge Id", required = false)
	private String knowledgeId;
	
	/** Knowledge Name  */
	@ApiModelProperty(value = "Knowledge Name", required = false)
	private String knowledgeName;
	
	/** MaterialVersion Code  */
	@ApiModelProperty(value = "MaterialVersion Code", required = false)
	private String materialVersion;
	
	/** MaterialVersion Name  */
	@ApiModelProperty(value = "MaterialVersion Name", required = false)
	private String materialVersionName;
	
	/** Knowledge SearchCode  */
	@ApiModelProperty(value = "Knowledge SearchCode", required = false)
	private String searchCode;
	
	@ApiModelProperty(value = "Knowledge NO", required = false)
	private String knowledgeNo;

	@ApiModelProperty(value = "Outline ID", required = false)
	private String outlineId;
}
