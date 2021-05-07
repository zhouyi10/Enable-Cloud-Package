package com.enableets.edu.pakage.framework.ppr.bo;

import com.enableets.edu.pakage.framework.ppr.paper.po.ExamInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamKindInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamQuestionChildInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamQuestionInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamQuestionTypeInfoPO;

import java.util.List;
import lombok.Data;

@Data
public class SaveExamBO {
	
	/** 试卷信息  */
	private ExamInfoPO exam;
	
	/** 试卷卷别信息list  */
	private List<ExamKindInfoPO> examKinds;
	
	/** 试卷题型信息list  */
	private List<ExamQuestionTypeInfoPO> examQuestionTypes;
	
	/** 试卷题目信息list  */
	private List<ExamQuestionInfoPO> examQuestions;
	
	/** 试卷小题信息list  */
	private List<ExamQuestionChildInfoPO> examQuestionChildren;

}
