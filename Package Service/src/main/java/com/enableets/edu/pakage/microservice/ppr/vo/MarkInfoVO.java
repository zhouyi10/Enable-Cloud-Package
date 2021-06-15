package com.enableets.edu.pakage.microservice.ppr.vo;

import com.enableets.edu.module.service.core.BaseVO;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * Mark Info object
 * @author walle_yu
 * @since 2020/10/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "markInfoVO", description = "Mark Info")
public class MarkInfoVO extends BaseVO {
	
	/** Test ID */
	@ApiModelProperty(value = "Test ID", required = true)
	private String testId;
	
	/** Paper ID */
	@ApiModelProperty(value = "Paper ID", required = true)
	private String examId;
	
	/** User Answer List*/
	@ApiModelProperty(value = "User Answer List", required = true)
	private List<MarkUserAnswerInfoVO> answers;
	
	/** Type   2 Temporary storage   1 Mark completed  3 Review completed (confirm that an exam review is completed)*/
	@ApiModelProperty(value = "Type", required = true)
	private Integer type;

	@Override
	public void validate() throws MicroServiceException {
		this.notBlank(testId, "testId");
		this.notNull(type, "type");
		if (type != null && type != PPRConstants.MARK_TYPE_ALL_COMPLETE) {
			this.notBlank(answers, "answers");
		}
	}

	@Data
	@ApiModel(value = "markUserAnswerInfoVO", description = "Review information-user answer review information object")
	public static class MarkUserAnswerInfoVO extends BaseVO {

		/** Answer ID */
		@ApiModelProperty(value = "Answer ID", required = true)
		private String answerId;
		
		/** User Submit Record ID */
		@ApiModelProperty(value = "User Submit Record ID", required = true)
		private String testUserId;
		
		/** Question ID */
		@ApiModelProperty(value = "Question ID", required = true)
		private String questionId;
		
		/** User ID */
		@ApiModelProperty(value = "User ID", required = true)
		private String userId;
		
		/** score */
		@ApiModelProperty(value = "score", required = true)
		private Float answerScore;
		
		/** Answer Status 0 Right   1 Wrong */
		@ApiModelProperty(value = "Answer Status 0 Right 1 Wrong", required = true)
		private Integer answerStatus;
		
		/** Mark Status  0 unMark    1 marked */
		@ApiModelProperty(value = "Mark Status  0 unMark    1 marked ", required = true)
		private Integer markStatus;

		@Override
		public void validate() throws MicroServiceException {
			this.notBlank(answerId, "answerId");
			this.notBlank(testUserId, "testUserId");
			this.notBlank(questionId, "questionId");
			this.notBlank(userId, "userId");
			this.notNull(answerScore, "answerScore");
			this.notNull(answerStatus, "answerStatus");
			this.notNull(markStatus, "markStatus");
		}
	}
}
