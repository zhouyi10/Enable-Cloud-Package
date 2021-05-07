package com.enableets.edu.pakage.framework.ppr.bo;

import com.enableets.edu.pakage.framework.bo.CodeNameMapBO;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * test paper structure questions
 * @author duffy_ding
 * @since 2017/12/29
 */
@Data
public class PPRQuestionBO {

	/** Question ID*/
	private String questionId;

	/** Super Question ID*/
	private String parentId;

	/** Question type  */
	private CodeNameMapBO type;

	/** stage info */
	private CodeNameMapBO stage;

	/** grade info */
	private CodeNameMapBO grade;

	/** subject info  */
	private CodeNameMapBO subject;

	/** ability info  */
	private CodeNameMapBO ability;

	/** difficulty info  */
	private CodeNameMapBO difficulty;

	/** knowledge list  */
	private List<QuestionKnowledgeInfoBO> knowledges;

	/** Question score  */
	private Float points;

	/** Number of question children  */
	private Integer childAmount;

	/** question content info  */
	private QuestionStemInfoBO stem;

	/** Option information  */
	private List<PaperQuestionOptionBO> options;

	/** Answer message  */
	private PaperQuestionAnswerBO answer;

	/** Listening text*/
	private String listen;

	/** Listening document */
	private String affixId;

	/** Question number*/
	private String questionNo;

	/** */
	private Integer sequencing;

	/** Question Axis*/
	private List<PPRQuestionAxisBO> axises;
}
