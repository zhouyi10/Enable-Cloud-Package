package com.enableets.edu.pakage.microservice.ppr.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * User Answer Info
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="QueryUserAnswerInfoVO", description="User Answer Info")
public class QueryUserAnswerInfoVO {
	
	/** Answer ID*/
	@ApiModelProperty(value="Answer ID")
	private String answerId;
	
	/** Paper ID*/
	@ApiModelProperty(value="Paper ID")
	private String examId;
	
	/** Question ID*/
	@ApiModelProperty(value="Question ID")
	private String questionId;

	/** Parent Question ID*/
	@ApiModelProperty(value = "Parent Question ID")
	private String parentId;
	
	/** User Answer*/
	@ApiModelProperty(value="User Answer")
	private String userAnswer;
	
	/** Answer Score*/
	@ApiModelProperty(value="Answer Score")
	private String answerScore;
	
	/** Answer Status(Right/Wrong)*/
	@ApiModelProperty(value="Answer Status(Right/Wrong)")
	private String answerStatus;
	
	/** Mark Status*/
	@ApiModelProperty(value="Mark Status")
	private String markStatus;
	
	/** Question Score*/
	@ApiModelProperty(value="Question Score")
	private Float questionScore;

	@ApiModelProperty(value = "User ID")
	private String userId;

	@ApiModelProperty(value = "User Name")
	private String userName;
	
	/** Drawing answer List*/
	@ApiModelProperty(value="Drawing answer List")
	private List<QueryUserAnswerCanvasInfoVO> canvases;
}
