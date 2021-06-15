package com.enableets.edu.pakage.manager.mark.core;

import com.enableets.edu.pakage.manager.core.Constants;
import com.enableets.edu.sdk.school3.dto.UserIdentityDTO;

/**
 * 公共方法
 * @author walle_yu@enable-ets.com
 * @since 2018/3/22
 */
public class CommFun {

	/**
	 * 判断是否是老师角色
	 * @param userIdentity
	 * @return
	 */
	public static boolean isTeacher(UserIdentityDTO userIdentity){
		return isContians(userIdentity.getIdentities(),Constants.TEACHER_IDENTITY_CODE);
	}
	
	/**
	 * 判断是否是学生角色
	 * @param userIdentity
	 * @return
	 */
	public static boolean isStudent(UserIdentityDTO userIdentity){
		return isContians(userIdentity.getIdentities(),Constants.STUDENT_IDENTITY_CODE);
	}
	
	/**
	 * 判断是否是管理员角色
	 * @param userIdentity
	 * @return
	 */
	public static boolean isAdmin(UserIdentityDTO userIdentity){
		return isContians(userIdentity.getIdentities(), Constants.ADMIN_IDENTITY_CODE);
	}
	
	private static boolean isContians(String[] arr,String key){
		if(arr == null || arr.length==0) return false;
		for(String str : arr){
			if(str.equals(key)){
				return true;
			}
		}
		return false;
	}
	
	public static String getSatgeCodeByGradeCode(String gradeCode){
		if(gradeCode.indexOf("21")==0) return "2";
		else if(gradeCode.indexOf("31")==0) return "3";
		else if(gradeCode.indexOf("34")==0) return "4";
		return "";
	}
	
}
