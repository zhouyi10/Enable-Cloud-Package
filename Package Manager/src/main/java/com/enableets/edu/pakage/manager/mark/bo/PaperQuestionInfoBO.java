package com.enableets.edu.pakage.manager.mark.bo;

import com.enableets.edu.pakage.manager.mark.core.CodeNameMap;
import com.enableets.edu.pakage.manager.mark.core.CodeNameMap;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * 试卷节点题目信息
 * @author duffy_ding
 * @since 2018/01/14
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaperQuestionInfoBO {

	/** 引用题目标识  */
	private String questionId;

	/** (导入成绩excel使用,其他地方使用请自行处理)如果为小题，此值为父题目标识 */
	private String parentQuestionId;

	/** 题目类型  */
	private CodeNameMap type;
	
	/** 学段标识  */
	private CodeNameMap stage;
	
	/** 年级标识  */
	private CodeNameMap grade;
	
	/** 学科标识  */
	private CodeNameMap subject;
	
	/** 能力标识  */
	private CodeNameMap ability;
	
	/** 难易度标识  */
	private CodeNameMap difficulty;
	
	/** 知识点信息列表  */
	private List<KnowledgeInfoBO> knowledgeList;
	
	/** 题目分数  */
	private Float points;
	
	/** 子题目个数  */
	private Integer childAmount;

	/** 题干信息  */
	private QuestionStemInfoBO stem;

	/** 选项信息  */
	private List<QuestionOptionInfoBO> options;

	/** 答案信息  */
	private AnswerInfoBO answer;

	/** 标签信息  */
	private List<CodeNameMap> tags;

	/** 听力附件标识 */
	private String affixId;

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getParentQuestionId() {
		return parentQuestionId;
	}

	public void setParentQuestionId(String parentQuestionId) {
		this.parentQuestionId = parentQuestionId;
	}

	public CodeNameMap getType() {
		return type;
	}

	public void setType(CodeNameMap type) {
		this.type = type;
	}

	public CodeNameMap getStage() {
		return stage;
	}

	public void setStage(CodeNameMap stage) {
		this.stage = stage;
	}

	public CodeNameMap getGrade() {
		return grade;
	}

	public void setGrade(CodeNameMap grade) {
		this.grade = grade;
	}

	public CodeNameMap getSubject() {
		return subject;
	}

	public void setSubject(CodeNameMap subject) {
		this.subject = subject;
	}

	public CodeNameMap getAbility() {
		return ability;
	}

	public void setAbility(CodeNameMap ability) {
		this.ability = ability;
	}

	public CodeNameMap getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(CodeNameMap difficulty) {
		this.difficulty = difficulty;
	}

	public List<KnowledgeInfoBO> getKnowledgeList() {
		return knowledgeList;
	}

	public void setKnowledgeList(List<KnowledgeInfoBO> knowledgeList) {
		this.knowledgeList = knowledgeList;
	}

	public Float getPoints() {
		return points;
	}

	public void setPoints(Float points) {
		this.points = points;
	}

	public Integer getChildAmount() {
		return childAmount;
	}

	public void setChildAmount(Integer childAmount) {
		this.childAmount = childAmount;
	}

	public QuestionStemInfoBO getStem() {
		return stem;
	}

	public void setStem(QuestionStemInfoBO stem) {
		this.stem = stem;
	}

	public List<QuestionOptionInfoBO> getOptions() {
		return options;
	}

	public void setOptions(List<QuestionOptionInfoBO> options) {
		this.options = options;
	}

	public AnswerInfoBO getAnswer() {
		return answer;
	}

	public void setAnswer(AnswerInfoBO answer) {
		this.answer = answer;
	}

	public List<CodeNameMap> getTags() {
		return tags;
	}

	public void setTags(List<CodeNameMap> tags) {
		this.tags = tags;
	}

	public String getAffixId() {
		return affixId;
	}

	public void setAffixId(String affixId) {
		this.affixId = affixId;
	}
}
