package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.Data;

/**
 * test paper structure questions
 * @author walle_yu
 * @since 2020/20/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaperQuestionDTO {

	/** Question ID*/
	private String questionId;

	/** Super Question ID*/
	private String parentId;

	/** Question type  */
	private CodeNameMapDTO type;

	/** stage info */
	private CodeNameMapDTO stage;

	/** grade info */
	private CodeNameMapDTO grade;

	/** subject info  */
	private CodeNameMapDTO subject;

	/** ability info  */
	private CodeNameMapDTO ability;
	
	/** difficulty info  */
	private CodeNameMapDTO difficulty;
	
	/** knowledge list  */
	private List<QuestionKnowledgeInfoDTO> knowledges;
	
	/** Question score  */
	private Float points;
	
	/** Number of question children  */
	private Integer childAmount;

	/** question content info  */
	private QuestionStemInfoDTO stem;

	/** Option information  */
	private List<QuestionOptionDTO> options;

	/** Answer message  */
	private QuestionAnswerDTO answer;

	/** Listening text*/
	private String listen;

	/** Listening document */
	private String affixId;

	/** Question number*/
	private String questionNo;

}
