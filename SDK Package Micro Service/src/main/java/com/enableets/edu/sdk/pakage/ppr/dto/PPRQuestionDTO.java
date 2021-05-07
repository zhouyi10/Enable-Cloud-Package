package com.enableets.edu.sdk.pakage.ppr.dto;

import java.util.List;
import lombok.Data;

/**
 * the structure questions
 * @author walle_yu
 * @since 2017/12/29
 */
@Data
public class PPRQuestionDTO {

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

	/** Question Axis*/
	private List<PPRQuestionAxisDTO> axises;
}
