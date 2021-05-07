package com.enableets.edu.pakage.framework.ppr.bo;

import java.util.List;
import lombok.Data;

/**
 * 活动指定人
 * @author tony_liu
 * @since 2019/11/06
 */
@Data
public class ActivityAssignerBO {

	//活动标识
	private String activityId;
	//活动指定人集合
	private List<ActivityTeacherBO> teachers;
	//创建者
	private String creator;
}
