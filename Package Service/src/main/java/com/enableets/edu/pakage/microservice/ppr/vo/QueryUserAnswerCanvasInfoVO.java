package com.enableets.edu.pakage.microservice.ppr.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Drawing answer information
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="QueryUserAnswerCanvasInfoVO", description="Drawing answer information")
public class QueryUserAnswerCanvasInfoVO {
	
	/** Canvas ID*/
	@ApiModelProperty(value="Canvas ID")
	private String canvasId;

	/** Canvas Type  0:Student Answer 1：Teacher's comment*/
	@ApiModelProperty(value="Canvas Type")
	private String canvasType;
	
	/** Doc Sequence*/
	@ApiModelProperty(value="Doc Sequence")
	private Integer canvasOrder;
	
	/** File ID*/
	@ApiModelProperty(value="File ID")
	private String fileId;
	
	/** File Name*/
	@ApiModelProperty(value="File Name")
	private String fileName;
	
	/** Resource ID*/
	@ApiModelProperty(value="Resource ID")
	private String contentId;

	/** Url*/
	@ApiModelProperty(value="Url")
	private String url;

	@ApiModelProperty(value = "Original document id")
	private String sourceFileId;

}
