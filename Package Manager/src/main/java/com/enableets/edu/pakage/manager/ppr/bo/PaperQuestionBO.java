package com.enableets.edu.pakage.manager.ppr.bo;

import com.enableets.edu.pakage.manager.bo.CodeNameMapBO;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * test paper structure questions
 * @author duffy_ding
 * @since 2017/12/29
 */
@Data
public class PaperQuestionBO {

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


	private Integer sequencing;

	/** Listening document */
	private String affixId;

	/** Question number*/
	private String questionNo;

	/** creator  */
	private String creator;

	/** create time  */
	private Date createTime;

	/** updater  */
	private String updator;

	/** update time  */
	private Date updateTime;

	/** Child Question*/
	private List<PaperQuestionBO> children;

	private Integer downloadNumber;

	/***/
	private List<QuestionAxisBO> axises;

}
