package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Edit canvasVO
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditCanvasInfoDTO{

	/** Primary key*/
	private String canvasId;
	
	/** Resource ID*/
	private String contentId;
	
	/** File ID*/
	private String fileId;
	
	/** File Name*/
	private String fileName;
	
	/** User ID*/
	private String userId;

	/** Url*/
	private String url;
}
