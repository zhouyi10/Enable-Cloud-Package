package com.enableets.edu.pakage.manager.mark.core;

import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.pakage.manager.core.Constants;
import com.enableets.edu.sdk.school3.*;
import com.enableets.edu.sdk.school3.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 获取用户标识/学校标识/当前学期标识
 * 
 * @author duffy_ding
 * @since 2017/09/21
 */
@Component
public class BaseInfoManager {
	
	/** logger  */
	private static final Logger logger = LoggerFactory.getLogger(BaseInfoManager.class);

	/** 用户角色sdk */
	@Autowired
	private IUserIdentityService userIdentityServiceSDK;

	/** 学期信息sdk */
	@Autowired
	private ITermInfoService termInfoServiceSDK;

	@Autowired
	private ICourseTeacherService  courseTeacherService;
	
	@Autowired
	private IClassInfoService classInfoService;

	/** 字典信息sdk */
	@Autowired
	private IDictionaryInfoService dictionaryInfoServiceSDK;


	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 *            用户标识
	 * @return
	 */
	public UserIdentityDTO getUserInfo(String userId) {
		/*if (StringUtils.isBlank(userId)) {
			userId = getUserId();
		}*/
		UserIdentityDTO userIdentity = new UserIdentityDTO();
		userIdentity.setUserId(userId);
		List<UserIdentityDTO> users = userIdentityServiceSDK.query(userIdentity);
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}

	/**
	 * 获取当前学校id
	 * 
	 * @param userId
	 * @return
	 */
	public String getSchoolId(String userId) {
		UserIdentityDTO userInfo = getUserInfo(userId);
		if (userInfo != null) {
			return userInfo.getSchoolId();
		}
		return null;
	}


	/**
	 * 返回当前学期信息
	 * 
	 * @param schoolId
	 *            学校标识
	 * @return
	 */
	public QueryTermInfoResultDTO getCurrentTerm(String schoolId) {
		List<QueryTermInfoResultDTO> terms = getTerms(schoolId);
		if (terms.size() > 0) {
			for (QueryTermInfoResultDTO term : terms) {
				if (term.getStatus().equals(Constants.STATUS_CURRENT_TERM)) {
					return term;
				}
			}
		}
		return null;
	}

	/**
	 * 获取学期信息list
	 * 
	 * @param schoolId
	 *            学校标识
	 * @return
	 */
	public List<QueryTermInfoResultDTO> getTerms(String schoolId) {
		if (StringUtils.isBlank(schoolId)) {
			schoolId = getSchoolId(null);
		}
		if (StringUtils.isNotBlank(schoolId)) {
			QueryTermInfoDTO queryTermInfo = new QueryTermInfoDTO();
			queryTermInfo.setSchoolId(schoolId);
			List<QueryTermInfoResultDTO> list = termInfoServiceSDK.query(queryTermInfo);
			if (list != null && list.size() > 0) {
				return list;
			}
		}
		return Collections.emptyList();
	}

	/**
	 * 查询用户的学段信息
	 * @param userId
	 * @return
	 */
	public String getUserStageInfo(String userId) {
		String stageCode = null;
		UserIdentityDTO userIdentityDTO = this.getUserInfo(userId);
		if (userIdentityDTO == null) return stageCode;
		if (CommFun.isTeacher(userIdentityDTO)){     //老师
			List<TeacherCourseInfoDTO> teacherCourses = courseTeacherService.queryByUserId(userId, userIdentityDTO.getSchoolId(), "");
			for (TeacherCourseInfoDTO teacherCourseInfoDTO : teacherCourses) {
				//如果classId不为空，表示是班级不是组
				if (StringUtils.isBlank(teacherCourseInfoDTO.getClassId())) continue;
				try {
					QueryClassInfoResultDTO classInfo = classInfoService.get(teacherCourseInfoDTO.getClassId(), false).getData();
					stageCode =  CommFun.getSatgeCodeByGradeCode(classInfo.getGradeCode());
					return stageCode;
				} catch (Exception e) {
					logger.error("获取教师班级学科出错：" + e.getMessage());
				}
			}
		}
		if (CommFun.isStudent(userIdentityDTO)){       //学生
			QueryClassInfoDTO classDTO = new QueryClassInfoDTO();
			classDTO.setSchoolId(userIdentityDTO.getSchoolId());
			classDTO.setStudentUserId(userId);
			List<QueryClassInfoResultDTO> classList = classInfoService.query(classDTO).getData();
			if (classList != null && classList.size() >0){
				QueryClassInfoResultDTO classInfo = classList.get(0);
				return CommFun.getSatgeCodeByGradeCode(classInfo.getGradeCode());
			}
		}
		if (CommFun.isAdmin(userIdentityDTO)){         //管理员
			
		}
		return "3";
	}


	public List<CodeNameWithRelationMap> getStage(String schoolId) {
		// 修改从school接口获取学科信息
		List<DictionaryInfoDTO> stageList = dictionaryInfoServiceSDK.query(schoolId, Constants.DICTIONARY_STAGE_TYPE_CODE).getData();
		if (org.springframework.util.CollectionUtils.isEmpty(stageList)) {
			return Collections.emptyList();
		}
		List<CodeNameWithRelationMap> result = new ArrayList<CodeNameWithRelationMap>();
		for (DictionaryInfoDTO dic : stageList) {
			result.add(new CodeNameWithRelationMap(dic.getCode(), dic.getName()));
		}
		return result;
	}

}
