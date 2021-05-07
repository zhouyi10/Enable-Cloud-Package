package com.enableets.edu.ppr.adapter.bo;

import com.enableets.edu.ppr.adapter.po.ExamKindPO;
import com.enableets.edu.ppr.adapter.po.ExamPO;
import com.enableets.edu.ppr.adapter.po.ExamQuestionChildPO;
import com.enableets.edu.ppr.adapter.po.ExamQuestionPO;
import com.enableets.edu.ppr.adapter.po.ExamQuestionTypePO;

import java.util.List;
import lombok.Data;

@Data
public class SaveExamBO {
	
	/** 试卷信息  */
	private ExamPO exam;
	
	/** 试卷卷别信息list  */
	private List<ExamKindPO> examKinds;
	
	/** 试卷题型信息list  */
	private List<ExamQuestionTypePO> examQuestionTypes;
	
	/** 试卷题目信息list  */
	private List<ExamQuestionPO> examQuestions;
	
	/** 试卷小题信息list  */
	private List<ExamQuestionChildPO> examQuestionChildren;

}
