package com.enableets.edu.pakage.manager.etm.service;

import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.pakage.framework.bo.CodeNameMapBO;
import com.enableets.edu.pakage.framework.core.Constants;
import com.enableets.edu.pakage.framework.etm.bo.BaseSearchConditionBO;
import com.enableets.edu.pakage.framework.ppr.bo.GradeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.StageInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.SubjectInfoBO;
import com.enableets.edu.sdk.content.dto.ContentDictionaryInfoDTO;
import com.enableets.edu.sdk.content.service.IContentDictionaryInfoService;
import com.enableets.edu.sdk.school3.IDictionaryInfoService;
import com.enableets.edu.sdk.school3.IGradeStageService;
import com.enableets.edu.sdk.school3.dto.DictionaryInfoDTO;
import com.enableets.edu.sdk.school3.dto.QueryGradeStageResultDTO;
import com.enableets.edu.sdk.school3.v2.dto.SubjectGradeV2DTO;
import com.enableets.edu.sdk.school3.v2.dto.knowledge.QuerySmsKnowledgeDTO;
import com.enableets.edu.sdk.school3.v2.dto.knowledge.SmsKnowledgeResultDTO;
import com.enableets.edu.sdk.school3.v2.service.knowledge.ISmsKnowledgeService;
import com.enableets.edu.sdk.school3.v2.service.subjectgrade.ISubjectGradeV2Service;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class ETMDictionaryInfoService {

    private static final Logger logger = LoggerFactory.getLogger(ETMDictionaryInfoService.class);

    /** Content SDK Client*/
    @Autowired
    private IContentDictionaryInfoService contentDictionaryInfoServiceSDK;


    /** School SDK Client*/
    @Autowired
    private ISmsKnowledgeService smsKnowledgeServiceSDK;


    /** School SDK Client*/
    @Autowired
    private IDictionaryInfoService dictionaryInfoServiceSDK;

    /** School SDK Client*/
    @Autowired
    private IGradeStageService gradeStageServiceSDK;

    /** School SDK Client*/
    @Autowired
    private ISubjectGradeV2Service subjectGradeV2Service;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * Content Dictionary Stage Info
     * @return List
     */
    public List<CodeNameMapBO> contentStage(){
        return contentDictionary(Constants.CONTENT_DICTIONARY_TYPE_STAGE);
    }

    /**
     * Get Stage Name By Code
     * @param code Code
     * @return
     */
    public String matchStageName(String code){
        return getName(Constants.CONTENT_DICTIONARY_TYPE_STAGE, code);
    }

    /**
     * Content Dictionary Grade Info
     * @return List
     */
    public List<CodeNameMapBO> contentGrade(){
        return contentDictionary(Constants.CONTENT_DICTIONARY_TYPE_GRADE);
    }

    /**
     * Get Grade Name By Code
     * @param code
     * @return
     */
    public String matchGradeName(String code){
        return getName(Constants.CONTENT_DICTIONARY_TYPE_GRADE, code);
    }

    /**
     * Content Dictionary Subject Info
     * @return List
     */
    public List<CodeNameMapBO> contentSubject(){
        return contentDictionary(Constants.CONTENT_DICTIONARY_TYPE_SUBJECT);
    }

    /**
     * Get Subject Name By Code
     * @param code Code
     * @return
     */
    public String matchSubjectName(String code){
        return getName(Constants.CONTENT_DICTIONARY_TYPE_SUBJECT, code);
    }

    /**
     * Content Dictionary MaterialVersion Info
     * @return
     */
    public List<CodeNameMapBO> contentMaterialVersion(){
        return contentDictionary(Constants.CONTENT_DICTIONARY_TYPE_MATERIAL_VERSION);
    }

    /**
     * Get Material Version Name By Code
     * @param code
     * @return
     */
    public String matchMaterialVersionName(String code){
        return getName(Constants.CONTENT_DICTIONARY_TYPE_MATERIAL_VERSION, code);
    }


    /**
     * Content Dictionary MaterialVersion Info
     * @return
     */
    public List<CodeNameMapBO> contentTerm(){
        return contentDictionary(Constants.CONTENT_DICTIONARY_TYPE_TERM);
    }

    /**
     * Get Material Version Name By Code
     * @param code
     * @return
     */
    public String matchTermName(String code){
        return getName(Constants.CONTENT_DICTIONARY_TYPE_TERM, code);
    }

    private String getName(String type, String code){
        List<CodeNameMapBO> codeNameMapBOS = this.contentDictionary(type);
        if (CollectionUtils.isEmpty(codeNameMapBOS)) return null;
        String name = null;
        for (CodeNameMapBO codeNameMapBO : codeNameMapBOS) {
            if (codeNameMapBO.getCode().equals(code)) {
                name = codeNameMapBO.getName(); break;
            }
        }
        return name;
    }

    public List<CodeNameMapBO> smsMaterialVersion(String gradeCode, String subjectCode){

        String materialVersionCacheKey = new StringBuilder("com:enableets:edu:package:etm:etm:materialVersion:").append(StringUtils.isBlank(gradeCode) ? "all" : gradeCode).append("_").append(StringUtils.isBlank(subjectCode) ? "all" : subjectCode).toString();
        String materialVersionStr = stringRedisTemplate.opsForValue().get(materialVersionCacheKey);
        if (StringUtils.isNotBlank(materialVersionStr)){
            return JsonUtils.convert2List(materialVersionStr, CodeNameMapBO.class);
        }else {
            QuerySmsKnowledgeDTO querySmsKnowledgeDTO = new QuerySmsKnowledgeDTO();
            querySmsKnowledgeDTO.setGradeCode(gradeCode);
            querySmsKnowledgeDTO.setSubjectCode(subjectCode);
            List<SmsKnowledgeResultDTO> materialVersions = smsKnowledgeServiceSDK.queryList(querySmsKnowledgeDTO);
            if (CollectionUtils.isNotEmpty(materialVersions)){
                List<CodeNameMapBO> materials = materialVersions.stream().map(e -> new CodeNameMapBO(e.getMaterialVersion(),e.getMaterialVersionName(), e.getGradeCode(), e.getSubjectCode())).collect(Collectors.toList());
                stringRedisTemplate.opsForValue().set(materialVersionCacheKey, JsonUtils.convert(materials), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
                return materials;
            }
        }
        return null;
    }


    /**
     * School Base Condition Info
     * @param schoolId
     * @return
     */
    @Cacheable(value = "com:enableets:edu:package:etm:etm:school:condition", key = "#schoolId")
    public BaseSearchConditionBO schoolConditionDictionary(String schoolId){
        if (StringUtils.isBlank(schoolId)) return null;
        List<DictionaryInfoDTO> stageInfos = dictionaryInfoServiceSDK.query(schoolId, "02", null, null, null, null, null);
        if (CollectionUtils.isEmpty(stageInfos)) return null;
        BaseSearchConditionBO condition = new BaseSearchConditionBO();
        condition.setStages(stageInfos.stream().map(e -> new StageInfoBO(e.getCode(), e.getName())).collect(Collectors.toList()));
        List<QueryGradeStageResultDTO> gradeStageInfos = gradeStageServiceSDK.query(schoolId, null, null).getData();
        if (CollectionUtils.isEmpty(gradeStageInfos)) {
            logger.error("Get Base Condition From School is Null, School Id is " + schoolId);
            return condition;
        }
        condition.setGrades(gradeStageInfos.stream().map(e -> new GradeInfoBO(e.getGradeCode(), e.getGradeName(), e.getStageCode())).collect(Collectors.toList()));
        List<SubjectGradeV2DTO> subjectInfos = subjectGradeV2Service.query(schoolId, null, null);
        if (CollectionUtils.isEmpty(subjectInfos)) return condition;
        List<SubjectInfoBO> subjects = new ArrayList<>();
        for (SubjectGradeV2DTO subjectInfo : subjectInfos) {
            subjects.add(new SubjectInfoBO(subjectInfo.getSubjectCode(),subjectInfo.getSubjectName(),subjectInfo.getGradeCode()));
        }
        condition.setSubjects(subjects);
        return condition;
    }

    /**
     * Get Base Info From Content
     * @param type Base Info Type
     * @return
     */
    private List<CodeNameMapBO> contentDictionary(String type){
        if (StringUtils.isBlank(type)) return null;
        String dictionaryKey = new StringBuilder("com:enableets:edu:package:etm:etm:content:dictionary:type:").append(type).toString();
        String jsonStr = stringRedisTemplate.opsForValue().get(dictionaryKey);
        if (StringUtils.isNotBlank(jsonStr)) return JsonUtils.convert2List(jsonStr, CodeNameMapBO.class);
        List<ContentDictionaryInfoDTO> dictionaries = contentDictionaryInfoServiceSDK.query(Constants.DEFAULT_SCHOOL, type, null, null, null, "0");//Query All
        if (CollectionUtils.isEmpty(dictionaries)) return null;
        List<CodeNameMapBO> dics = dictionaries.stream().map(e -> new CodeNameMapBO(e.getCode(), e.getName())).collect(Collectors.toList());
        stringRedisTemplate.opsForValue().set(dictionaryKey, JsonUtils.convert(dics), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
        return dics;
    }
}
