package com.enableets.edu.pakage.manager.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.pakage.manager.bo.CodeNameMapBO;
import com.enableets.edu.pakage.manager.bo.IdNameMapBO;
import com.enableets.edu.pakage.manager.ppr.service.DictionaryInfoService;
import com.enableets.edu.sdk.content.dto.ContentDictionaryInfoDTO;
import com.enableets.edu.sdk.school3.IClassInfoService;
import com.enableets.edu.sdk.school3.ICourseInfoService;
import com.enableets.edu.sdk.school3.ICourseTeacherService;
import com.enableets.edu.sdk.school3.IGradeStageService;
import com.enableets.edu.sdk.school3.ISchoolInfoService;
import com.enableets.edu.sdk.school3.IUserIdentityService;
import com.enableets.edu.sdk.school3.dto.GetCourseInfoResultDTO;
import com.enableets.edu.sdk.school3.dto.QueryGradeStageResultDTO;
import com.enableets.edu.sdk.school3.dto.Response;
import com.enableets.edu.sdk.school3.dto.SchoolInfoDTO;
import com.enableets.edu.sdk.school3.dto.TeacherCourseInfoDTO;
import com.enableets.edu.sdk.school3.dto.UserIdentityDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Get User Info
 * @author walle_yu@enable-ets.com
 * @since 2020/08/10
 **/
@Service
public class BaseInfoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseInfoService.class);

    /** School Client SDK*/
    @Autowired
    private IUserIdentityService userIdentityServiceSDK;

    @Autowired
    private ISchoolInfoService schoolInfoServiceSDK;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ICourseTeacherService courseTeacherServiceSDK;

    @Autowired
    private ICourseInfoService courseInfoServiceSDK;

    @Autowired
    private IGradeStageService gradeStageServiceSDK;

    @Autowired
    private DictionaryInfoService dictionaryInfoService;

    /**
     * School User Info
     * @param userId User ID
     * @return User ID:Name
     */
    public IdNameMapBO getUserInfo(String userId){
        UserIdentityDTO userInfo = this.userInfo(userId);
        if (userInfo == null) return new IdNameMapBO();
        return new IdNameMapBO(userInfo.getUserId(), userInfo.getFullName());
    }

    /**
     * School User School Info
     * @param userId User ID
     * @return School ID:Name
     */
    public IdNameMapBO getUserSchoolInfo(String userId){
        UserIdentityDTO userInfo = this.userInfo(userId);
        if (userInfo == null) return new IdNameMapBO();
        return new IdNameMapBO(userInfo.getSchoolId(), userInfo.getSchoolName());
    }

    /**
     * Get User Area SearchCode
     * @param userId User ID
     * @return
     */
    public String getUserZone(String userId){
        SchoolInfoDTO school = new SchoolInfoDTO();
        school.setSchoolId(this.getUserSchoolInfo(userId).getId());
        List<SchoolInfoDTO> list = schoolInfoServiceSDK.query(school);
        String zoneCode = "R01";
        if (CollectionUtils.isNotEmpty(list)){
            SchoolInfoDTO schoolInfoDTO = list.get(0);
            zoneCode += "-" + schoolInfoDTO.getProvinceCode() + "-" + schoolInfoDTO.getCityCode() + "-" + schoolInfoDTO.getAreaCode();
        }
        return zoneCode;
    }

    public String getStageByGrade(String gradeCode, String schoolId){
        List<CodeNameMapBO> gradeStageRelation = this.getGradeStageRelation(schoolId);
        if (CollectionUtils.isNotEmpty(gradeStageRelation)){
            String stageCode = "";
            for (CodeNameMapBO codeNameMapBO : gradeStageRelation) {
                if (codeNameMapBO.getCode().equals(gradeCode)) {
                    stageCode = codeNameMapBO.getRelationCode(); break;
                }
            }
            return stageCode;
        }
        return null;
    }

    public Map<String, String> getTeacherInfo(String userId, boolean isObligatoryCourse){
        UserIdentityDTO userIdentityDTO = this.userInfo(userId);
        if (userIdentityDTO == null || userIdentityDTO.getSchool() == null || userIdentityDTO.getSchool().getTerm() == null) {
            return null;
        }
        String schoolId = userIdentityDTO.getSchool().getId();
        String termId = userIdentityDTO.getSchool().getTerm().getId();
        // 查询所教课程信息
        List<TeacherCourseInfoDTO> teacherCourses = courseTeacherServiceSDK.queryByUserId(userId, schoolId, termId);
        if (CollectionUtils.isEmpty(teacherCourses)) {
            return null;
        }
        if (isObligatoryCourse) {
            teacherCourses = teacherCourses.stream().filter(course -> {
                return Constants.COURSE_SELECTION_TYPE_REQUIRED.equals(course.getSelectionType())
                        && Constants.COURSE_TEACHING_METHOD_OFFLINE.equals(course.getTeachingMethod())
                        && com.enableets.edu.framework.core.util.StringUtils.isNotBlank(course.getClassCode());
            }).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(teacherCourses)) {
            return null;
        }
        TeacherCourseInfoDTO dto = teacherCourses.get(0);
        Map<String, String> baseInfo = new HashMap<>();
        baseInfo.put("schoolId", schoolId);
        baseInfo.put("schoolName", userIdentityDTO.getSchool().getName());
        baseInfo.put("termId", termId);
        baseInfo.put("termName", userIdentityDTO.getSchool().getTerm().getName());
        String stageCode = this.getStageByGrade(dto.getGradeCode(), schoolId);
        baseInfo.put("stageCode", stageCode);
        baseInfo.put("stageName", dictionaryInfoService.matchStageName(stageCode));
        baseInfo.put("gradeCode", dto.getGradeCode());
        baseInfo.put("gradeName", dto.getGradeName());
        baseInfo.put("subjectCode", dto.getSubjectCode());
        baseInfo.put("subjectName", dto.getSubjectName());
        try {
            GetCourseInfoResultDTO courseInfo = courseInfoServiceSDK.getById(dto.getCourseId(), dto.getSchoolId()).getData();
            baseInfo.put("materialVersion", courseInfo.getCourseTextbooks().get(0).getTextbookVersionId());
        } catch (Exception e) {
            LOGGER.error("GET Teacher TextBook Error！" + e.getMessage());
            baseInfo.put("materialVersion", com.enableets.edu.framework.core.util.StringUtils.EMPTY);
        }
        return baseInfo;
    }

    private UserIdentityDTO userInfo(String userId){
        if (StringUtils.isBlank(userId)) return null;
        String userInfoCacheKey = "com:enableets:edu:package:school:user:" + userId;
        String userInfo = stringRedisTemplate.opsForValue().get(userInfoCacheKey);
        if (StringUtils.isNotBlank(userInfo)) return JsonUtils.convert(userInfo, UserIdentityDTO.class);
        List<UserIdentityDTO> users = userIdentityServiceSDK.query(userId, null, null, null, null, null, null, null, null);
        if (CollectionUtils.isEmpty(users)) return null;
        else{
            stringRedisTemplate.opsForValue().set(userInfoCacheKey, JsonUtils.convert(users.get(0)),Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            return users.get(0);
        }
    }

    private List<CodeNameMapBO> getGradeStageRelation(String schoolId){
        if (StringUtils.isBlank(schoolId)) schoolId = Constants.DEFAULT_SCHOOL;
        String key = "com:enableets:edu:package:school:grade&stage:" + schoolId;
        String cacheInfo = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(cacheInfo)) return JsonUtils.convert2List(cacheInfo, CodeNameMapBO.class);
        List<QueryGradeStageResultDTO> gradeStageDTOs = gradeStageServiceSDK.query(schoolId, null, null).getData();
        if (CollectionUtils.isNotEmpty(gradeStageDTOs)){
            List<CodeNameMapBO> list = gradeStageDTOs.stream().map(e -> new CodeNameMapBO(e.getGradeCode(), e.getGradeName(), e.getStageCode())).collect(Collectors.toList());
            stringRedisTemplate.opsForValue().set(key, JsonUtils.convert(list),Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            return list;
        }
        return null;
    }
}
