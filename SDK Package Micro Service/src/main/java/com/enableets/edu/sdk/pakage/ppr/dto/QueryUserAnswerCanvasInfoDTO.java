package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Drawing answer information
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryUserAnswerCanvasInfoDTO {
	
	/** Canvas ID*/
	private String canvasId;

	/** Canvas Type  0:Student Answer 1：Teacher's comment*/
	private String canvasType;
	
	/** Doc Sequence*/
	private Integer canvasOrder;
	
	/** File ID*/
	private String fileId;
	
	/** File Name*/
	private String fileName;
	
	/** Resource ID*/
	private String contentId;

	/** Url*/
	private String url;

	private String sourceFileId;

}
