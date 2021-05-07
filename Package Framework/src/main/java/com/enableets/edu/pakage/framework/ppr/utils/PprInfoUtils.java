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
import com.enableets.edu.pakage.framework.ppr.bo.PPRInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PPRNodeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PPRQuestionAxisBO;
import com.enableets.edu.pakage.framework.ppr.bo.PPRQuestionBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperQuestionAnswerBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperQuestionOptionBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionKnowledgeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionOptionInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionStemInfoBO;
import com.enableets.edu.pakage.framework.ppr.core.DictionaryInfoService;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Paper convert tools
 */
public class PprInfoUtils {

	private static DictionaryInfoService dictionaryInfoService = SpringBeanUtils.getBean(DictionaryInfoService.class);

	/**
	 *  Convert test paper information
	 *
	 * @param exam Paper Info
	 * @return PaperInfoBO
	 */
	public static PPRInfoBO transformExam2Paper(ExamInfoBO exam) {
		if (exam == null) {
			return null;
		}
		PPRInfoBO pprInfoBO = new PPRInfoBO();
		pprInfoBO.setPaperId(Long.valueOf(exam.getExamId()));
		if (StringUtils.isNotBlank(exam.getContentId())) {
			pprInfoBO.setContentId(Long.parseLong(exam.getContentId()));
		}
		pprInfoBO.setName(exam.getExamName());
		pprInfoBO.setGrade(new CodeNameMapBO(exam.getGradeCode(), exam.getGradeName()));
		pprInfoBO.setSubject(new CodeNameMapBO(exam.getSubjectCode(), exam.getSubjectName()));
		pprInfoBO.setMaterialVersion(new IdNameMapBO(exam.getMaterialVersionId(), dictionaryInfoService.matchMaterialVersionName(exam.getMaterialVersionId())));
		pprInfoBO.setTotalPoints(exam.getScore());
		pprInfoBO.setAnswerCostTime(exam.getAnswerCostTime());
		if (!CollectionUtils.isEmpty(exam.getExamKinds())) {
			pprInfoBO.setNodes(transformExamKind2Node(exam.getExamKinds()));
		}
		pprInfoBO.setUser(new IdNameMapBO(exam.getCreator(), null));
		pprInfoBO.setPaperType(exam.getExamType());
		pprInfoBO.setCreateTime(exam.getCreateTime());
		return pprInfoBO;
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
	private static PPRQuestionBO transformQues2PaperQuestion(QuestionInfoBO question) {
		if (StringUtils.isBlank(question.getQuestionId())) {
			return null;
		}
		PPRQuestionBO pprQuestion = new PPRQuestionBO();
		pprQuestion.setQuestionId(question.getQuestionId());
		if (StringUtils.isNotBlank(question.getParentId())) {
			pprQuestion.setParentId(question.getParentId());
		}
		if (StringUtils.isNotBlank(question.getGradeId())) {
			pprQuestion.setGrade(new CodeNameMapBO(question.getGradeId(), question.getGradeName()));
		}
		if (StringUtils.isNotBlank(question.getSubjectId())) {
			pprQuestion.setSubject(new CodeNameMapBO(question.getSubjectId(), question.getSubjectName()));
		}
		if (StringUtils.isNotBlank(question.getAbilityId())) {
			pprQuestion.setAbility(new CodeNameMapBO(question.getAbilityId(), dictionaryInfoService.matchAbilityName(question.getAbilityId())));
		}
		if (StringUtils.isNotBlank(question.getQuestionTypeId())) {
			pprQuestion.setType(new CodeNameMapBO(question.getQuestionTypeId(), dictionaryInfoService.matchQuestionTypeName(question.getQuestionTypeId())));
		}
		pprQuestion.setDifficulty(new CodeNameMapBO(question.getQuestionDifficulty(), dictionaryInfoService.matchDifficultyName(question.getQuestionDifficulty())));
		QuestionStemInfoBO stem = new QuestionStemInfoBO();
		pprQuestion.setStem(stem);
		if (StringUtils.isNotBlank(question.getQuestionContent())) {
			stem.setPlaintext(question.getQuestionContentNoHtml());
			stem.setRichText(question.getQuestionContent());
		}
		pprQuestion.setPoints(question.getScore());
		PaperQuestionAnswerBO answer = new PaperQuestionAnswerBO();
		answer.setQuestionId(Long.parseLong(question.getQuestionId()));
		answer.setAnalysis(question.getAnswerAnalyse());
		if (StringUtils.isNotBlank(question.getAnswerContent())) {
			answer.setLabel(question.getAnswer());
			answer.setStrategy(question.getAnswerContent());
		}
		pprQuestion.setAnswer(answer);
		pprQuestion.setChildAmount(question.getChildCount());
		pprQuestion.setOptions(transformQuesOption(question.getOptions()));
		pprQuestion.setKnowledges(BeanUtils.convert(question.getKnowledges(), QuestionKnowledgeInfoBO.class));
		pprQuestion.setSequencing(question.getQuestionChildOrder());
		pprQuestion.setAffixId(question.getAffixId());
		pprQuestion.setListen(question.getListen());
		pprQuestion.setQuestionNo(question.getQuestionNo());
		if (!CollectionUtils.isEmpty(question.getAxises())){
			pprQuestion.setAxises(BeanUtils.convert(question.getAxises(), PPRQuestionAxisBO.class));
		}
		return pprQuestion;
	}

	/**
	 * Convert child Question  of test paper  to node information
	 * 
	 * @param quesChildList Child Question Info
	 * @param internalNo Internal No.
	 * @return List
	 */
	public static List<PPRNodeInfoBO> transformExamQuesChild2Node(List<ExamQuestionChildInfoBO> quesChildList, Integer internalNo) {
		List<PPRNodeInfoBO> result = new ArrayList<>();
		for (ExamQuestionChildInfoBO bo : quesChildList) {
			if (StringUtils.isBlank(bo.getExamQuestionChildId())) {
				continue;
			}
			PPRNodeInfoBO node = new PPRNodeInfoBO();
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
	private static List<PPRNodeInfoBO> transformExamQues2Node(List<ExamQuestionInfoBO> quesList) {
		List<PPRNodeInfoBO> result = new ArrayList<>();
		for (ExamQuestionInfoBO bo : quesList) {
			if (StringUtils.isBlank(bo.getExamQuestionId())) {
				continue;
			}
			PPRNodeInfoBO node = new PPRNodeInfoBO();
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
	private static List<PPRNodeInfoBO> transformExamQuesType2Node(List<ExamQuestionTypeInfoBO> quesTypes) {
		List<PPRNodeInfoBO> result = new ArrayList<>();
		for (ExamQuestionTypeInfoBO bo : quesTypes) {
			if (StringUtils.isBlank(bo.getExamQuestionTypeId())) {
				continue;
			}
			PPRNodeInfoBO node = new PPRNodeInfoBO();
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
	private static List<PPRNodeInfoBO> transformExamKind2Node(List<ExamKindInfoBO> examKinds) {
		List<PPRNodeInfoBO> list = new ArrayList<>();
		for (ExamKindInfoBO bo : examKinds) {
			if (StringUtils.isBlank(bo.getExamKindId())) {
				continue;
			}
			PPRNodeInfoBO node = new PPRNodeInfoBO();
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
}
