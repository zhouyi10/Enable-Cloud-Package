package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.Data;

/**
 * Mark Info object
 * @author walle_yu
 * @since 2020/10/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarkInfoDTO {
	
	/** Test ID */
	private String testId;
	
	/** User Answer List*/
	private List<MarkUserAnswerInfoDTO> answers;
	
	/** Type   2 Temporary storage   1 Mark completed  3 Review completed (confirm that an exam review is completed)*/
	private Integer type;

	@Data
	public static class MarkUserAnswerInfoDTO{

		/** Answer ID */
		private String answerId;
		
		/** User Submit Record ID */
		private String testUserId;
		
		/** Question ID */
		private String questionId;
		
		/** User ID */
		private String userId;
		
		/** score */
		private Float answerScore;
		
		/** Answer Status 0 Right   1 Wrong */
		private Integer answerStatus;
		
		/** Mark Status  0 unMark    1 marked */
		private Integer markStatus;

	}
}
