package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.Data;

/**
 * User Answer Info
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryUserAnswerInfoDTO {
	
	/** Answer ID*/
	private String answerId;
	
	/** Paper ID*/
	private String examId;
	
	/** Question ID*/
	private String questionId;

	/** Parent Question ID*/
	private String parentId;
	
	/** User Answer*/
	private String userAnswer;
	
	/** Answer Score*/
	private String answerScore;
	
	/** Answer Status(Right/Wrong)*/
	private String answerStatus;
	
	/** Mark Status*/
	private String markStatus;
	
	/** Question Score*/
	private Float questionScore;

	private String userId;

	private String userName;
	
	/** Drawing answer List*/
	private List<QueryUserAnswerCanvasInfoDTO> canvases;
}
