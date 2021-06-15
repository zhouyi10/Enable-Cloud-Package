package com.enableets.edu.pakage.manager.mark.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 查询考试列表参数VO
 * @author walle_yu@enable-ets.com
 * @since 2018年6月8日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class QueryTestInfoVO {

	/** 用户标识*/
	private String userId;

	/** 年级标识*/
	private String gradeCode;

	/** 科目标识*/
	private String subjectCode;

	/** 考试名称*/
	private String testName;

	/** 考试类型*/
	private String testType;

	/** 考试时间戳*/
	private Long timestamp;

	/** 试卷类型*/
	private String examType;

	/** 批阅状态*/
	private String markStatus;

	/** 0：我派发的 1：指派给我的*/
	private String actionType;

	/** 分页查询-起始页*/
	private Integer offset;

	/** 分页查询-页容量*/
	private Integer rows;

	@Override
	public String toString() {
		return "TestInfoQueryVo{" +
				"userId='" + userId + '\'' +
				", gradeCode='" + gradeCode + '\'' +
				", subjectCode='" + subjectCode + '\'' +
				", testName='" + testName + '\'' +
				", testType='" + testType + '\'' +
				", timestamp=" + timestamp +
				", examType='" + examType + '\'' +
				", markStatus='" + markStatus + '\'' +
				", offset=" + offset +
				", rows=" + rows +
				'}';
	}
}
