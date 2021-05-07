package com.enableets.edu.pakage.framework.ppr.bo;

import lombok.Data;

/**
 * 活动指定人
 * @author tony_liu
 * @since 2019/11/06
 */
@Data
public class ActivityTeacherBO {

	//活动指派人标识
	private String userId;

	//活动指派人名称
	private String  fullName;

	//活动步骤，如1:批阅、2:查看报表等
	private String action;
}
