package com.enableets.edu.pakage.microservice.ppr.vo;

import com.enableets.edu.module.service.core.BaseVO;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Edit canvasVO
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="EditCanvasInfoVO", description="Edit canvasVO")
public class EditCanvasInfoVO extends BaseVO {
	
	@Override
	public void validate() throws MicroServiceException {
		this.notBlank(getCanvasId(), "canvasId");
		//this.validate(getContentId(), "contentId");
		this.notBlank(getFileId(), "fileId");
		this.notBlank(getFileName(), "fileName");
		this.notBlank(getUserId(), "userId");
	}
	
	/** Primary key*/
	@ApiModelProperty(value="Primary key", required=true)
	private String canvasId;
	
	/** Resource ID*/
	@ApiModelProperty(value="Resource ID", required=false)
	private String contentId;
	
	/** File ID*/
	@ApiModelProperty(value="File ID", required=true)
	private String fileId;
	
	/** File Name*/
	@ApiModelProperty(value="File Name", required=true)
	private String fileName;
	
	/** User ID*/
	@ApiModelProperty(value="User ID", required=true)
	private String userId;

	/** Url*/
	@ApiModelProperty(value="Url", required =true)
	private String url;
}
