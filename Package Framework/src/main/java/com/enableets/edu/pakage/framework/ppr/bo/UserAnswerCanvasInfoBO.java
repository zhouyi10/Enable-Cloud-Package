package com.enableets.edu.pakage.framework.ppr.bo;

import java.util.Date;
import lombok.Data;

/**
 *
 */
@Data
public class UserAnswerCanvasInfoBO {
	
	/** Key ID*/
	private String canvasId;
	
	/** Answer ID*/
	private String answerId;
	
	/** Canvas type：0-student answer，1-teacher comment*/
	private String canvasType;
	
	/** Canvas Order*/
	private Integer canvasOrder;
	
	/** Data Point*/
	private String content;
	
	/** */
	private String creator;
	
	/** */
	private Date createTime;
	
	/** */
	private String updator;
	
	/** */
	private Date updateTime;
	
	/** Answer type: 0, drawing answer, 1 taking photo answer ,2 video*/
	private String canvasAnswerType;

	/** File ID*/
	private String fileId;

	/** File Name*/
	private String fileName;

	/** File Download Url*/
	private String url;

	/** Original Document Identification*/
	private String sourceFileId;

	/** */
	private String userId;

}
