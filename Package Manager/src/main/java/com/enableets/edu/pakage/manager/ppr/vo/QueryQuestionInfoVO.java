package com.enableets.edu.pakage.manager.ppr.vo;

import lombok.Data;

/**
 * Search Question Info VO
 * @author walle_yu@enable-ets.com
 * @since 2020-08-17
 */
@Data
public class QueryQuestionInfoVO {
	
	/** 学段编码*/
	private String stageCode;
	
	/** 年级编码 */
	private String gradeCode;
	
	/** 科目编码*/
	private String subjectCode;
	
	/** 版本编码*/
	private String materialVersion;
	
	/** 难易度编码*/
	private String difficultyCode;
	
	/** 题型编码*/
	private String typeCode;
	
	/** 能力编码*/
	private String abilityCode;
	
	/** 关键字*/
	private String keyword;
	
	/** 知识点检索*/
	private String searchCode;
	
	/** 题号*/
	private String questionNo;
	
	/** 资源来源*/
	private String providerCode;
	
	/** 已选题目*/
	private String selectedQuestion;
	
	/** 选题方式 :_SINGLE _MULTIPLE*/
	private String selectType;
	
	/** 要更换的题目标识*/
	private String changeQuestionId;

	private String creator;

	/**  逻辑删除标识 */
	private String status;

	private String offset;
	
	private String rows;
}
