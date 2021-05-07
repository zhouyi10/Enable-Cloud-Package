package com.enableets.edu.pakage.framework.ppr.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.pakage.framework.bo.CodeNameMapBO;
import com.enableets.edu.pakage.framework.bo.IdNameMapBO;
import com.enableets.edu.pakage.framework.ppr.bo.ExamInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.ExamKindInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.ExamQuestionChildInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.ExamQuestionInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.ExamQuestionTypeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperNodeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperQuestionAnswerBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperQuestionBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperQuestionOptionBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionKnowledgeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionOptionInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionStemInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.SaveExamBO;
import com.enableets.edu.pakage.framework.ppr.core.DictionaryInfoService;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamKindInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamQuestionChildInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamQuestionInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamQuestionTypeInfoPO;
import com.enableets.edu.sdk.paper.util.dto.Exam;
import com.enableets.edu.sdk.paper.util.dto.ExamKind;
import com.enableets.edu.sdk.paper.util.dto.ExamQuestion;
import com.enableets.edu.sdk.paper.util.dto.ExamQuestionChild;
import com.enableets.edu.sdk.paper.util.dto.ExamQuestionType;
import com.enableets.edu.sdk.paper.util.dto.Options;
import com.enableets.edu.sdk.paper.util.dto.Question;
import com.enableets.edu.sdk.paper.util.dto.QuestionChild;
import com.enableets.edu.sdk.paper.util.dto.QuestionOption;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Paper convert tools
 */
public class PaperInfoUtils {

	private static DictionaryInfoService dictionaryInfoService = SpringBeanUtils.getBean(DictionaryInfoService.class);

	/**
	 *  Convert test paper information
	 *
	 * @param exam Paper Info
	 * @return PaperInfoBO
	 */
	public static PaperInfoBO transformExam2Paper(ExamInfoBO exam) {
		if (exam == null) {
			return null;
		}
		PaperInfoBO paperInfo = new PaperInfoBO();
		paperInfo.setPaperId(Long.valueOf(exam.getExamId()));
		if (StringUtils.isNotBlank(exam.getContentId())) {
			paperInfo.setContentId(Long.parseLong(exam.getContentId()));
		}
		paperInfo.setName(exam.getExamName());
		paperInfo.setGrade(new CodeNameMapBO(exam.getGradeCode(), exam.getGradeName()));
		paperInfo.setSubject(new CodeNameMapBO(exam.getSubjectCode(), exam.getSubjectName()));
		paperInfo.setMaterialVersion(new IdNameMapBO(exam.getMaterialVersionId(), dictionaryInfoService.matchMaterialVersionName(exam.getMaterialVersionId())));
		paperInfo.setTotalPoints(exam.getScore());
		paperInfo.setAnswerCostTime(exam.getAnswerCostTime());
		if (!CollectionUtils.isEmpty(exam.getExamKinds())) {
			paperInfo.setNodes(transformExamKind2Node(exam.getExamKinds()));
		}
		paperInfo.setUser(new IdNameMapBO(exam.getCreator(), null));
		paperInfo.setCreator(exam.getCreator());
		paperInfo.setCreateTime(exam.getCreateTime());
		paperInfo.setUpdator(exam.getUpdator());
		paperInfo.setUpdateTime(exam.getUpdateTime());
		paperInfo.setPaperType(exam.getExamType());
		return paperInfo;
	}



	/**
	 * Convert question option information Question option information
	 * @return List
	 */
	public static List<PaperQuestionOptionBO> transformQuesOption(List<QuestionOptionInfoBO> questionOptions) {
		if (CollectionUtils.isEmpty(questionOptions)) {
			return Collections.emptyList();
		}
		List<PaperQuestionOptionBO> options = new ArrayList<>();
		for (QuestionOptionInfoBO bo : questionOptions) {
			if (StringUtils.isBlank(bo.getOptionId())) {
				continue;
			}
			PaperQuestionOptionBO option = new PaperQuestionOptionBO();
			option.setOptionId(Long.parseLong(bo.getOptionId()));
			if (StringUtils.isNotBlank(bo.getQuestionId())) {
				option.setQuestionId(Long.parseLong(bo.getQuestionId()));
			}
			option.setAlias(bo.getOptionTitle());
			option.setLabel(bo.getOptionContent());
			option.setSequencing(bo.getOptionOrder());
			options.add(option);
		}
		return options;
	}

	/**
	 * Convert 2.0 question information to 3.0 test paper question information
	 * 
	 * @param question 题目信息
	 * @return PaperQuestionBO
	 */
	private static PaperQuestionBO transformQues2PaperQuestion(QuestionInfoBO question) {
		if (StringUtils.isBlank(question.getQuestionId())) {
			return null;
		}
		PaperQuestionBO paperQuestion = new PaperQuestionBO();
		paperQuestion.setQuestionId(question.getQuestionId());
		if (StringUtils.isNotBlank(question.getParentId())) {
			paperQuestion.setParentId(question.getParentId());
		}
		if (StringUtils.isNotBlank(question.getGradeId())) {
			paperQuestion.setGrade(new CodeNameMapBO(question.getGradeId(), question.getGradeName()));
		}
		if (StringUtils.isNotBlank(question.getSubjectId())) {
			paperQuestion.setSubject(new CodeNameMapBO(question.getSubjectId(), question.getSubjectName()));
		}
		if (StringUtils.isNotBlank(question.getAbilityId())) {
			paperQuestion.setAbility(new CodeNameMapBO(question.getAbilityId(), dictionaryInfoService.matchAbilityName(question.getAbilityId())));
		}
		if (StringUtils.isNotBlank(question.getQuestionTypeId())) {
			paperQuestion.setType(new CodeNameMapBO(question.getQuestionTypeId(), dictionaryInfoService.matchQuestionTypeName(question.getQuestionTypeId())));
		}
		paperQuestion.setDifficulty(new CodeNameMapBO(question.getQuestionDifficulty(), dictionaryInfoService.matchDifficultyName(question.getQuestionDifficulty())));
		QuestionStemInfoBO stem = new QuestionStemInfoBO();
		paperQuestion.setStem(stem);
		if (StringUtils.isNotBlank(question.getQuestionContent())) {
			stem.setPlaintext(question.getQuestionContentNoHtml());
			stem.setRichText(question.getQuestionContent());
		}
		paperQuestion.setPoints(question.getScore());
		PaperQuestionAnswerBO answer = new PaperQuestionAnswerBO();
		answer.setQuestionId(Long.parseLong(question.getQuestionId()));
		answer.setAnalysis(question.getAnswerAnalyse());
		if (StringUtils.isNotBlank(question.getAnswerContent())) {
			answer.setLabel(question.getAnswer());
			answer.setStrategy(question.getAnswerContent());
		}
		paperQuestion.setAnswer(answer);
		paperQuestion.setChildAmount(question.getChildCount());
		paperQuestion.setOptions(transformQuesOption(question.getOptions()));
		paperQuestion.setKnowledges(BeanUtils.convert(question.getKnowledges(), QuestionKnowledgeInfoBO.class));
		paperQuestion.setSequencing(question.getQuestionChildOrder());
		paperQuestion.setAffixId(question.getAffixId());
		paperQuestion.setListen(question.getListen());
		paperQuestion.setCreator(question.getCreator());
		paperQuestion.setCreateTime(question.getCreateTime());
		paperQuestion.setUpdator(question.getUpdator());
		paperQuestion.setUpdateTime(question.getUpdateTime());
		paperQuestion.setQuestionNo(question.getQuestionNo());
		return paperQuestion;
	}

	/**
	 * Convert child Question  of test paper  to node information
	 * 
	 * @param quesChildList Child Question Info
	 * @param internalNo Internal No.
	 * @return List
	 */
	public static List<PaperNodeInfoBO> transformExamQuesChild2Node(List<ExamQuestionChildInfoBO> quesChildList, Integer internalNo) {
		List<PaperNodeInfoBO> result = new ArrayList<>();
		for (ExamQuestionChildInfoBO bo : quesChildList) {
			if (StringUtils.isBlank(bo.getExamQuestionChildId())) {
				continue;
			}
			PaperNodeInfoBO node = new PaperNodeInfoBO();
			node.setNodeId(Long.parseLong(bo.getExamQuestionChildId()));
			node.setParentNodeId(Long.parseLong(bo.getExamQuestionId()));
			node.setLevel(PPRConstants.LEVEL_EXAM_QUES_CHILD);
			node.setInternalNo(result.size() + 1);
			node.setExternalNo(bo.getQuestionChildNo());
			node.setPoints(bo.getScore());
			node.setSequencing(++internalNo);
			if (bo.getQuestion() != null) {
				node.setQuestion(transformQues2PaperQuestion(bo.getQuestion()));
			}
			result.add(node);
		}
		return result;
	}

	/**
	 * Convert Exam question info of test paper  to node information
	 * 
	 * @param quesList Questions
	 * @return
	 */
	private static List<PaperNodeInfoBO> transformExamQues2Node(List<ExamQuestionInfoBO> quesList) {
		List<PaperNodeInfoBO> result = new ArrayList<>();
		for (ExamQuestionInfoBO bo : quesList) {
			if (StringUtils.isBlank(bo.getExamQuestionId())) {
				continue;
			}
			PaperNodeInfoBO node = new PaperNodeInfoBO();
			node.setNodeId(Long.parseLong(bo.getExamQuestionId()));
			node.setParentNodeId(Long.parseLong(bo.getExamQuestionTypeId()));
			node.setLevel(PPRConstants.LEVEL_EXAM_QUES);
			node.setInternalNo(bo.getQuestionOrder());
			node.setExternalNo(bo.getQuestionNo());
			node.setPoints(bo.getScore());
			node.setSequencing(bo.getQuestionOrder());
			if (bo.getQuestion() != null) {
				node.setQuestion(transformQues2PaperQuestion(bo.getQuestion()));
			}
			result.add(node);
			if (!CollectionUtils.isEmpty(bo.getExamQuestionChildren())) {
				result.addAll(transformExamQuesChild2Node(bo.getExamQuestionChildren(), bo.getQuestionOrder()));
			}
		}
		return result;
	}

	/**
	 * Convert question type information of test paper to node information
	 * 
	 * @param quesTypes
	 *            试卷题型信息
	 * @return
	 */
	private static List<PaperNodeInfoBO> transformExamQuesType2Node(List<ExamQuestionTypeInfoBO> quesTypes) {
		List<PaperNodeInfoBO> result = new ArrayList<>();
		for (ExamQuestionTypeInfoBO bo : quesTypes) {
			if (StringUtils.isBlank(bo.getExamQuestionTypeId())) {
				continue;
			}
			PaperNodeInfoBO node = new PaperNodeInfoBO();
			node.setNodeId(Long.parseLong(bo.getExamQuestionTypeId()));
			node.setParentNodeId(Long.parseLong(bo.getExamKindId()));
			node.setName(bo.getDescription());
			node.setLevel(PPRConstants.LEVEL_EXAM_QUES_TYPE);
			node.setInternalNo(bo.getQuestionTypeNo());
			node.setExternalNo(String.valueOf(bo.getQuestionTypeNo()));
			node.setSequencing(bo.getQuestionTypeNo());
			node.setPoints(bo.getScore());
			result.add(node);
			if (!CollectionUtils.isEmpty(bo.getExamQuestions())) {
				result.addAll(transformExamQues2Node(bo.getExamQuestions()));
			}
		}
		return result;
	}

	/**
	 * Convert test paper kind to node information list
	 * 
	 * @param examKinds
	 *            试卷卷别信息
	 * @return
	 */
	private static List<PaperNodeInfoBO> transformExamKind2Node(List<ExamKindInfoBO> examKinds) {
		List<PaperNodeInfoBO> list = new ArrayList<>();
		for (ExamKindInfoBO bo : examKinds) {
			if (StringUtils.isBlank(bo.getExamKindId())) {
				continue;
			}
			PaperNodeInfoBO node = new PaperNodeInfoBO();
			node.setParentNodeId(Long.parseLong(bo.getExamId()));
			node.setNodeId(Long.parseLong(bo.getExamKindId()));
			node.setParentNodeId(null);
			node.setName(bo.getDescription());
			node.setLevel(PPRConstants.LEVEL_EXAM_KIND);
			node.setInternalNo(bo.getKindNo());
			node.setExternalNo(String.valueOf(bo.getKindNo()));
			node.setSequencing(bo.getKindNo());
			node.setPoints(bo.getScore());
			list.add(node);
			if (!CollectionUtils.isEmpty(bo.getExamQuestionTypes())) {
				list.addAll(transformExamQuesType2Node(bo.getExamQuestionTypes()));
			}
		}
		return list;
	}

	/**
	 * Set question to paper Info
	 * 
	 * @param exam Paper Info
	 * @param questionList Questions
	 * @return
	 */
	public static ExamInfoBO mergeExamAndQuestion(ExamInfoBO exam, List<QuestionInfoBO> questionList) {
		if (exam == null || CollectionUtils.isEmpty(questionList)) {
			return exam;
		}
		Map<String, QuestionInfoBO> quesMap = new HashMap<String, QuestionInfoBO>();
		for (QuestionInfoBO bo : questionList) {
			quesMap.put(bo.getQuestionId(), bo);
		}
		if (!CollectionUtils.isEmpty(exam.getExamKinds())) {
			for (ExamKindInfoBO kind : exam.getExamKinds()) {
				if (!CollectionUtils.isEmpty(kind.getExamQuestionTypes())) {
					for (ExamQuestionTypeInfoBO type : kind.getExamQuestionTypes()) {
						if (!CollectionUtils.isEmpty(type.getExamQuestions())) {
							for (ExamQuestionInfoBO ques : type.getExamQuestions()) {
								QuestionInfoBO questionP = processQuestionHtml(quesMap.get(ques.getQuestionId()));
								ques.setQuestion(questionP);
								if (!CollectionUtils.isEmpty(ques.getExamQuestionChildren())) {
									for (ExamQuestionChildInfoBO child : ques.getExamQuestionChildren()) {
										QuestionInfoBO questionC = processQuestionHtml(quesMap.get(child.getQuestionChildId()));
										if (questionC == null) continue;
										questionC.setKnowledges(questionP.getKnowledges());
										child.setQuestion(questionC);
									}
								}
							}
						}
					}
				}
			}
		}
		return exam;
	}

	/**
	 * Handler url in question
	 * @param question Question Info
	 * @return
	 */
	private static QuestionInfoBO processQuestionHtml(QuestionInfoBO question) {
		if (question != null && StringUtils.isBlank(question.getAnswer()) && StringUtils.isNotBlank(question.getAnswerContent())){
			question.setAnswer(Utils.unescapeHtmlTags(question.getAnswerContent().replaceFirst(PPRConstants.QUESTION_ANSWER_CONTENT_REPLACE_REGEX, "")));
		}
		return question;
	}

	public static Exam buildExamXmlBO(PaperInfoBO paperInfo) {
		Exam exam = new Exam();
		exam.setExamId(paperInfo.getPaperId().toString());
		exam.setExanName(paperInfo.getName());
		exam.setExamType("2");
		exam.setScore(paperInfo.getTotalPoints().toString());
		if (paperInfo.getGrade() != null) {
			exam.setGradeId(paperInfo.getGrade().getCode());
		}
		if (paperInfo.getSubject() != null) {
			exam.setSubjectId(paperInfo.getSubject().getCode());
			exam.setSubjectName(paperInfo.getSubject().getName());
		}
		exam.getExamKind().addAll(buildStructureElement(paperInfo.getNodes()));
		return exam;
	}

	private static List<ExamKind> buildStructureElement(List<PaperNodeInfoBO> nodes) {
		if (CollectionUtils.isEmpty(nodes)) {
			return null;
		}
		List<ExamKind> examKindList = new ArrayList<ExamKind>();
		ExamKind lastKind = null;
		ExamQuestionType lastType = null;
		ExamQuestion lastQuestion = null;
		for (PaperNodeInfoBO node : nodes) {
			switch (node.getLevel()) {
				case PPRConstants.LEVEL_EXAM_KIND:
					ExamKind examKind = new ExamKind();
					examKind.setKindNo(node.getSequencing().toString());
					examKind.setDescription(node.getName());
					examKind.setScore(node.getPoints().toString());
					examKindList.add(examKind);
					lastKind = examKind;
					break;
				case PPRConstants.LEVEL_EXAM_QUES_TYPE:
					ExamQuestionType type = new ExamQuestionType();
					type.setQuestionTypeNo(node.getSequencing().toString());
					type.setDescription(node.getName());
					type.setScore(node.getPoints().toString());
					lastKind.getExamQuestionType().add(type);
					lastType = type;
					break;
				case PPRConstants.LEVEL_EXAM_QUES:
					ExamQuestion examQues = new ExamQuestion();
					examQues.setExamQuestionId(node.getNodeId().toString());
					examQues.setQuestionNo(node.getExternalNo());
					examQues.setQuestionOrder(node.getInternalNo().toString());
					PaperQuestionBO question = node.getQuestion();
					if (node.getQuestion() != null && com.enableets.edu.framework.core.util.StringUtils.isNotBlank(node.getQuestion().getQuestionId())) {
						examQues.setQuestionId(node.getQuestion().getQuestionId());
					}
					examQues.setScore(node.getPoints().toString());
					examQues.setAffixId(node.getQuestion().getAffixId());
					examQues.setQuestion(translateQuestion(node.getQuestion(), examQues.getQuestionId()));
					lastType.getExamQuestion().add(examQues);
					lastQuestion = examQues;
					break;
				case PPRConstants.LEVEL_EXAM_QUES_CHILD:
					ExamQuestionChild examQuesChild = new ExamQuestionChild();
					examQuesChild.setExamQuestionChildId(node.getNodeId().toString());
					if (node.getQuestion() != null && com.enableets.edu.framework.core.util.StringUtils.isNotBlank(node.getQuestion().getQuestionId())) {
						examQuesChild.setQuestionChildId(node.getQuestion().getQuestionId());
					}
					examQuesChild.setQuestionChildNo(node.getExternalNo());
					examQuesChild.setScore(node.getPoints().toString());
					examQuesChild.setQuestionChild(translateQuestionChild(node.getQuestion(), examQuesChild.getQuestionChildId()));
					lastQuestion.getExamQuestionChild().add(examQuesChild);
					break;
			}
		}
		return examKindList;
	}

	/**
	 * 转换子题目信息为xml对象
	 * @param question
	 * @param questionId 题目标识
	 * @return
	 */
	private static QuestionChild translateQuestionChild(PaperQuestionBO question, String questionId) {
		if (question == null) {
			return null;
		}
		QuestionChild ques = new QuestionChild();
		if (question.getType() != null) {
			CodeNameMapBO type = question.getType();
			ques.setQuestionType(type.getCode());
			ques.setQuestionTypeId(type.getCode());
			ques.setQuestionTypeName(type.getName());
		}
		if (question.getDifficulty() != null) {
			ques.setQuestionDifficulty(question.getDifficulty().getCode());
		}
		ques.setDescription("");//
		QuestionStemInfoBO quesStem = question.getStem();
		if (quesStem != null) {
			ques.setQuestionContent(quesStem.getRichText());
			ques.setQuestionContentNoHtml(quesStem.getPlaintext());
		}
		PaperQuestionAnswerBO quesAnswer = question.getAnswer();
		if (quesAnswer != null) {
			ques.setAnswerContent(quesAnswer.getStrategy());
			ques.setAnswer(quesAnswer.getLabel());
		}
		ques.setScore(question.getPoints().toString());
		ques.setStepScore("");
		ques.setAvgScore("");
		ques.setDiagnose("");
		ques.setEstimateTime("");
		ques.setListen("");
		ques.getQuestionOption().addAll(translateOption(question.getOptions(), questionId));
		return ques;
	}

	private static Question translateQuestion(PaperQuestionBO question, String questionId) {
		if (question == null) {
			return null;
		}
		Question ques = new Question();
		if (question.getType() != null) {
			CodeNameMapBO type = question.getType();
			ques.setQuestionType(type.getCode());
			ques.setQuestionTypeId(type.getCode());
			if (com.enableets.edu.framework.core.util.StringUtils.isNotBlank(type.getName())) {
				ques.setQuestionTypeName(type.getName());
			} else {
				if (com.enableets.edu.framework.core.util.StringUtils.isNotBlank(type.getCode())) {
					ques.setQuestionTypeName(dictionaryInfoService.matchQuestionTypeName(type.getCode()));
				} else {
					ques.setQuestionTypeName("");
				}
			}
		}
		if (question.getDifficulty() != null) {
			ques.setQuestionDifficulty(question.getDifficulty().getCode());
		}
		ques.setDescription("");//
		QuestionStemInfoBO quesStem = question.getStem();
		if (quesStem != null) {
			ques.setQuestionContent(quesStem.getRichText());
			ques.setQuestionContentNoHtml(quesStem.getPlaintext());
		}
		PaperQuestionAnswerBO quesAnswer = question.getAnswer();
		if (quesAnswer != null) {
			ques.setAnswerContent(quesAnswer.getStrategy());
			ques.setAnswer(quesAnswer.getLabel());
		}
		String knowledgeId = "";
		String knowledgeName = "";
		List<QuestionKnowledgeInfoBO> knowledges = question.getKnowledges();
		if (knowledges!=null) {
			for (int i = 0; i < knowledges.size(); i++) {
				knowledgeId = knowledgeId + knowledges.get(i).getKnowledgeId();
				knowledgeName = knowledgeName + knowledges.get(i).getKnowledgeName();
				if (knowledges.size() > (i+1)) {
					knowledgeId = knowledgeId + "@@@";
					knowledgeName = knowledgeName + "@@@";
				}
			}
		}
		ques.setKnowledgeId(knowledgeId);
		ques.setKnowledgeName(knowledgeName);
		ques.setScore(question.getPoints().toString());
		ques.setStepScore("");
		ques.setAvgScore("");
		ques.setDiagnose("");
		ques.setEstimateTime("");
		ques.setListen("");
		ques.getQuestionOption().addAll(translateOption(question.getOptions(), questionId));
		return ques;
	}

	/**
	 * 转换题目选项信息为xml对象
	 *
	 * @param options
	 * @param questionId
	 * @return
	 */
	private static List<QuestionOption> translateOption(List<PaperQuestionOptionBO> options, String questionId) {
		if (options == null || options.isEmpty()) {
			return Collections.emptyList();
		}
		List<QuestionOption> list = new ArrayList<QuestionOption>();
		for (PaperQuestionOptionBO bo : options) {
			String optionId = "";
			if (bo.getOptionId() != null) {
				optionId = bo.getOptionId().toString();
			}
			QuestionOption op = new QuestionOption();
			op.setOptionId(optionId);
			op.setQuestionId(questionId);
			Options option = new Options();
			option.setOptionId(optionId);
			option.setOptionOrder(bo.getSequencing().toString());
			option.setOptionTitle(bo.getAlias());
			option.setOptionContent(bo.getLabel());
			op.setOptions(option);
			list.add(op);
		}
		return list;
	}


	public static SaveExamBO transformExam(PaperInfoBO paperInfo) {
		// 1 拼装试卷
		SaveExamBO result = new SaveExamBO();
		if (paperInfo == null) {
			return result;
		}
		ExamInfoPO exam = buildExam(paperInfo);
		result.setExam(exam);

		List<PaperNodeInfoBO> nodes = paperInfo.getNodes();
		if (CollectionUtils.isEmpty(paperInfo.getNodes())) { // 无节点信息
			return result;
		}
		String examId = exam.getExamId();
		// 2 数据生成
		List<ExamKindInfoPO> examKindList = new ArrayList<>();
		List<ExamQuestionTypeInfoPO> examQuesTypeList = new ArrayList<>();
		List<ExamQuestionInfoPO> examQuesList = new ArrayList<>();
		List<ExamQuestionChildInfoPO> examQuesChildList = new ArrayList<>();
		for (PaperNodeInfoBO node : nodes) {
			node.setCreateTime(exam.getCreateTime());
			node.setUpdateTime(exam.getUpdateTime());
			node.setCreator(exam.getCreator());
			node.setUpdator(exam.getUpdator());
			switch (node.getLevel()) {
				case PPRConstants.LEVEL_EXAM_KIND:
					ExamKindInfoPO kind = buildExamKind(examId, node);
					examKindList.add(kind);
					break;
				case PPRConstants.LEVEL_EXAM_QUES_TYPE:
					ExamQuestionTypeInfoPO quesType = buildExamQuesType(node);
					examQuesTypeList.add(quesType);
					break;
				case PPRConstants.LEVEL_EXAM_QUES:
					ExamQuestionInfoPO question = buildExamQues(node);
					examQuesList.add(question);
					break;
				case PPRConstants.LEVEL_EXAM_QUES_CHILD:
					ExamQuestionChildInfoPO childQues = buildExamQuesChild(node);
					examQuesChildList.add(childQues);
			}
		}
		// 3 数据组装
		result.setExamKinds(examKindList);
		result.setExamQuestionTypes(examQuesTypeList);
		result.setExamQuestions(examQuesList);
		result.setExamQuestionChildren(examQuesChildList);
		// 4 返回结果
		return result;
	}

	/**
	 * 生成试卷信息
	 *
	 * @param paperInfo
	 *            新结构试卷数据
	 * @return
	 */
	private static ExamInfoPO buildExam(PaperInfoBO paperInfo) {
		Date today = Calendar.getInstance().getTime();
		ExamInfoPO exam = new ExamInfoPO();
		if (paperInfo.getPaperId() != null) {
			exam.setExamId(paperInfo.getPaperId().toString());
		}
		exam.setContentId(exam.getExamId()); //使用版本控制后，资源标识与试卷标识相同
		if (paperInfo.getContentId() != null) {
			exam.setContentId(paperInfo.getContentId().toString());
		}
		exam.setExamName(paperInfo.getName());
		CodeNameMapBO grade = paperInfo.getGrade();
		if (grade != null) {
			exam.setGradeCode(grade.getCode());
			exam.setGradeName(grade.getName());
		}

		CodeNameMapBO subject = paperInfo.getSubject();
		if (subject != null) {
			exam.setSubjectCode(subject.getCode());
			exam.setSubjectName(subject.getName());
		}

		if (paperInfo.getMaterialVersion() != null){
			exam.setMaterialVersionId(paperInfo.getMaterialVersion().getId());
		}

		if (com.enableets.edu.framework.core.util.StringUtils.isEmpty(paperInfo.getPaperType())) {
			exam.setExamType("2");
		} else {
			exam.setExamType(paperInfo.getPaperType());
		}
		exam.setDelStatus("0");
		exam.setScore(paperInfo.getTotalPoints());
		exam.setAnswerCostTime(100l);
		exam.setCreateTime(today);
		exam.setUpdateTime(today);
		exam.setCreator(paperInfo.getUser().getId());
		exam.setUpdator(paperInfo.getUser().getId());
		return exam;
	}

	private static ExamKindInfoPO buildExamKind(String examId, PaperNodeInfoBO node) {
		ExamKindInfoPO kind = new ExamKindInfoPO();
		kind.setExamId(examId);
		kind.setExamKindId(node.getNodeId().toString());
		kind.setKindNo(node.getSequencing());
		kind.setDescription(node.getName());
		kind.setScore(node.getPoints());
		kind.setCreateTime(node.getCreateTime());
		kind.setUpdateTime(node.getUpdateTime());
		kind.setCreator(node.getCreator());
		kind.setUpdator(node.getUpdator());
		return kind;
	}

	private static ExamQuestionTypeInfoPO buildExamQuesType(PaperNodeInfoBO node) {
		ExamQuestionTypeInfoPO quesType = new ExamQuestionTypeInfoPO();
		quesType.setExamKindId(node.getParentNodeId().toString());
		quesType.setExamQuestionTypeId(node.getNodeId().toString());
		quesType.setQuestionTypeNo(node.getSequencing());
		quesType.setDescription(node.getName());
		quesType.setCreateTime(node.getCreateTime());
		quesType.setUpdateTime(node.getUpdateTime());
		quesType.setScore(node.getPoints());
		quesType.setCreator(node.getCreator());
		quesType.setUpdator(node.getUpdator());
		return quesType;
	}

	private static ExamQuestionInfoPO buildExamQues(PaperNodeInfoBO node) {
		ExamQuestionInfoPO question = new ExamQuestionInfoPO();
		question.setExamQuestionTypeId(node.getParentNodeId().toString());
		question.setExamQuestionId(node.getNodeId().toString());
		if (com.enableets.edu.framework.core.util.StringUtils.isNotBlank(node.getQuestion().getQuestionId())) {
			question.setQuestionId(node.getQuestion().getQuestionId());
		}
		question.setQuestionNo(node.getExternalNo());
		question.setQuestionOrder(node.getInternalNo());
		question.setScore(node.getPoints());
		question.setCreateTime(node.getCreateTime());
		question.setUpdateTime(node.getUpdateTime());
		question.setCreator(node.getCreator());
		question.setUpdator(node.getUpdator());
		return question;
	}

	private static ExamQuestionChildInfoPO buildExamQuesChild(PaperNodeInfoBO node) {
		ExamQuestionChildInfoPO childQues = new ExamQuestionChildInfoPO();
		childQues.setExamQuestionChildId(node.getNodeId().toString());
		childQues.setExamQuestionId(node.getParentNodeId().toString());
		if (com.enableets.edu.framework.core.util.StringUtils.isNotBlank(node.getQuestion().getQuestionId())) {
			childQues.setQuestionChildId(node.getQuestion().getQuestionId());
		}
		childQues.setQuestionChildNo(node.getExternalNo());
		childQues.setScore(node.getPoints());
		childQues.setCreateTime(node.getCreateTime());
		childQues.setUpdateTime(node.getUpdateTime());
		childQues.setCreator(node.getCreator());
		childQues.setUpdator(node.getUpdator());
		return childQues;
	}
}
