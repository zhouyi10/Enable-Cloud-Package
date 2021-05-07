package com.enableets.edu.pakage.microservice.ppr.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.enableets.edu.pakage.framework.core.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * test paper structure questions
 * @author duffy_ding
 * @since 2017/12/29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "PaperQuestionVO", description = "Node Question Info")
public class PaperQuestionVO {

	/** Question ID*/
	@ApiModelProperty(value = "Question ID", required = false)
	private String questionId;

	/** Super Question ID*/
	@ApiModelProperty(value = "Parent Question ID", required = false)
	private String parentId;

	/** Question type  */
	@ApiModelProperty(value = "Question Type Info", required = false)
	private CodeNameMapVO type;

	/** stage info */
	@ApiModelProperty(value = "Question Stage Info", required = false)
	private CodeNameMapVO stage;

	/** grade info */
	@ApiModelProperty(value = "Question Grade Info", required = false)
	private CodeNameMapVO grade;

	/** subject info  */
	@ApiModelProperty(value = "Question Subject Info", required = false)
	private CodeNameMapVO subject;

	/** ability info  */
	@ApiModelProperty(value = "Question Ability Info", required = false)
	private CodeNameMapVO ability;
	
	/** difficulty info  */
	@ApiModelProperty(value = "Question Difficulty Info", required = false)
	private CodeNameMapVO difficulty;
	
	/** knowledge list  */
	@ApiModelProperty(value = "Question Knowledge Info", required = false)
	private List<QuestionKnowledgeInfoVO> knowledges;
	
	/** Question score  */
	@ApiModelProperty(value = "Question Points", required = false)
	private Float points;
	
	/** Number of question children  */
	@ApiModelProperty(value = "Children Question Amount", required = false)
	private Integer childAmount;

	/** question content info  */
	@ApiModelProperty(value = "Question Stem Info", required = false)
	private QuestionStemInfoVO stem;

	/** Option information  */
	@ApiModelProperty(value = "Question Option List", required = false)
	private List<PaperQuestionOptionVO> options;

	/** Answer message  */
	@ApiModelProperty(value = "Question Answer Info", required = false)
	private PaperQuestionAnswerVO answer;

	/** Listening text*/
	@ApiModelProperty(value = "Listening text", required = false)
	private String listen;

	/** Listening document */
	@ApiModelProperty(value = "Hearing File Url", required = false)
	private String affixId;

	/** Question number*/
	@ApiModelProperty(value = "Question No.", required = false)
	private String questionNo;

}
