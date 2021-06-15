package com.enableets.edu.pakage.framework.ppr.test.bo;

import lombok.Data;

import java.util.Date;

/**
 * 考试接收对象BO
 * @author walle_yu@enable-ets.com
 * @since 2018年5月31日
 */
@Data
public class TAsTestRecipientBO {
	
	/** 考试标识*/
	private Long testId;
	
	/** 考试活动标识*/
	private String activityId;
	
	/** 试卷文件标识*/
	private String fileId;
	
	/** 用户标识*/
	private String userId;
	
	/** 用户名称*/
	private String userName;
	
	/** 学校标识*/
	private String schoolId;
	
	/** 学校名称*/
	private String schoolName;
	
	/** 学期标识*/
	private String termId;
	
	/** 学期名称*/
	private String termName;
	
	/** 年级编码*/
	private String gradeCode;
	
	/** 年级名称*/
	private String gradeName;
	
	/** 班级标识*/
	private String classId;
	
	/** 班级名称*/
	private String className;	
	
	/** 群组标识*/
	private String groupId;
	
	/** 群组名称*/
	private String groupName;

	/** 交卷标识*/
	private String testUserId;

	/** 交卷状态*/
	private String status;

	/** 交卷时间*/
	private Date submitTime;
}
