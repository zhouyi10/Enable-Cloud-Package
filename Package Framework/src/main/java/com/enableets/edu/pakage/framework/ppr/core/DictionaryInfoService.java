package com.enableets.edu.pakage.framework.ppr.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.pakage.framework.bo.CodeNameMapBO;
import com.enableets.edu.pakage.framework.core.Constants;
import com.enableets.edu.pakage.framework.ppr.test.dao.SubjectAbilityInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.SubjectQuestionTypeInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.po.SubjectAbilityInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.SubjectQuestionTypeInfoPO;
import com.enableets.edu.pakage.framework.ppr.bo.BaseSearchConditionBO;
import com.enableets.edu.pakage.framework.ppr.bo.ContentKnowledgeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.GradeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.StageInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.SubjectInfoBO;
import com.enableets.edu.sdk.content.dto.ContentDictionaryInfoDTO;
import com.enableets.edu.sdk.content.dto.ContentKnowledgeInfoDTO;
import com.enableets.edu.sdk.content.dto.ContentMaterialVersionInfoDTO;
import com.enableets.edu.sdk.content.service.IContentDictionaryInfoService;
import com.enableets.edu.sdk.content.service.IContentInfoService;
import com.enableets.edu.sdk.school3.IOrgSubjectService;
import com.enableets.edu.sdk.school3.dto.QuerySubjectResultDTO;
import com.enableets.edu.sdk.school3.v2.dto.QueryDictionaryInfoDTO;
import com.enableets.edu.sdk.school3.v2.dto.QueryGradeStageResultDTO;
import com.enableets.edu.sdk.school3.v2.service.dictionary.IDictionaryInfoV2Service;
import com.enableets.edu.sdk.school3.v2.service.grade.IGradeInfoV2Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import tk.mybatis.mapper.entity.Example;

/**
 * Base Info Service
 * @author walle_yu@enable-ets.com
 * @since 2020/08/06
 **/
@Service
public class DictionaryInfoService {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryInfoService.class);

    /** Content SDK Client*/
    @Autowired
    private IContentDictionaryInfoService contentDictionaryInfoServiceSDK;

    /** Content SDK Client*/
    @Autowired
    private IContentInfoService contentInfoServiceSDK;

    /** School SDK Client*/
    @Autowired
    private IDictionaryInfoV2Service dictionaryInfoV2ServiceSDK;

    /** School SDK Client*/
    @Autowired
    private IGradeInfoV2Service gradeInfoV2ServiceSDK;

    /** School SDK Client*/
    @Autowired
    private IOrgSubjectService orgSubjectServiceSDK;

    @Autowired
    private SubjectQuestionTypeInfoDAO subjectQuestionTypeInfoDAO;

    @Autowired
    private SubjectAbilityInfoDAO subjectAbilityInfoDAO;

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
     * Content Dictionary Question Type Info
     * @return
     */
    public List<CodeNameMapBO> contentQuestionType(){
        return contentDictionary(Constants.CONTENT_DICTIONARY_TYPE_QUESTION_TYPE);
    }

    /**
     * Get Question Type Name By Id
     */
    public String matchQuestionTypeName(String id){
        return getName(Constants.CONTENT_DICTIONARY_TYPE_QUESTION_TYPE, id);
    }

    /**
     * Get Question Type About Subject
     * @param subjectCode
     * @return
     */
    public List<CodeNameMapBO> subjectQuestionType(String subjectCode){
        String subjectQuestionTypeCacheKey = null;
        if (StringUtils.isNotBlank(subjectCode))
            subjectQuestionTypeCacheKey = new StringBuilder("com:enableets:edu:package:ppr:paper:question:type:").append(subjectCode).toString();
        else subjectQuestionTypeCacheKey = new StringBuilder("com:enableets:edu:package:ppr:paper:question:type:").append("_all").toString();
        String typeStr = stringRedisTemplate.opsForValue().get(subjectQuestionTypeCacheKey);
        if (StringUtils.isNotBlank(typeStr)) {
            return JsonUtils.convert2List(typeStr, CodeNameMapBO.class);
        }else{
            Example example = new Example(SubjectQuestionTypeInfoPO.class);
            example.createCriteria().andEqualTo("subjectId", subjectCode);
            example.orderBy("questionTypeOrder").asc();
            List<SubjectQuestionTypeInfoPO> subjectQuestionTypeInfoPOS = subjectQuestionTypeInfoDAO.selectByExample(example);
            if (CollectionUtils.isEmpty(subjectQuestionTypeInfoPOS)) return null;
            List<CodeNameMapBO> types = subjectQuestionTypeInfoPOS.stream().map(e -> new CodeNameMapBO(e.getQuestionTypeId(), matchQuestionTypeName(e.getQuestionTypeId()), e.getSubjectId())).collect(Collectors.toList());
            stringRedisTemplate.opsForValue().set(subjectQuestionTypeCacheKey, JsonUtils.convert(types), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            return types;
        }
    }

    /**
     * Content Dictionary Ability Info
     * @return
     */
    public List<CodeNameMapBO> contentAbility(){
        return contentDictionary(Constants.CONTENT_DICTIONARY_TYPE_ABILITY);
    }

    /**
     * Get Ability Name by Id
     * @param id
     * @return
     */
    public String matchAbilityName(String id){
        return getName(Constants.CONTENT_DICTIONARY_TYPE_ABILITY, id);
    }

    /**
     * Subject Relation Ability
     * @return
     */
    public List<CodeNameMapBO> subjectAbility(String subjectCode){
        String subjectQuestionAbilityCacheKey = null;
        if (StringUtils.isNotBlank(subjectCode))
            subjectQuestionAbilityCacheKey = new StringBuilder("com:enableets:edu:package:ppr:paper:question:ability:").append(subjectCode).toString();
        else subjectQuestionAbilityCacheKey  = new StringBuilder("com:enableets:edu:package:ppr:paper:question:ability:").append("_all").toString();
        String jsonStr = stringRedisTemplate.opsForValue().get(subjectQuestionAbilityCacheKey);
        if (StringUtils.isNotBlank(jsonStr)) return JsonUtils.convert2List(jsonStr, CodeNameMapBO.class);
        else{
            Example example = new Example(SubjectAbilityInfoPO.class);
            example.createCriteria().andEqualTo("subjectId", subjectCode);
            List<SubjectAbilityInfoPO> subjectAbilityInfoPOS = subjectAbilityInfoDAO.selectByExample(example);
            if (CollectionUtils.isNotEmpty(subjectAbilityInfoPOS)){
                List<CodeNameMapBO> codeNameMapBOS = subjectAbilityInfoPOS.stream().map(e -> new CodeNameMapBO(e.getAbilityId(), this.matchAbilityName(e.getAbilityId()), e.getSubjectId())).collect(Collectors.toList());
                stringRedisTemplate.opsForValue().set(subjectQuestionAbilityCacheKey, JsonUtils.convert(codeNameMapBOS), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
                return codeNameMapBOS;
            }
        }
        return null;
    }

    /**
     * Content Dictionary Difficulty Info
     * @return
     */
    public List<CodeNameMapBO> contentDifficulty(){
        return contentDictionary(Constants.CONTENT_DICTIONARY_TYPE_DIFFICULTY);
    }

    /**
     * Get Difficulty Name by Id
     * @param id
     * @return
     */
    public String matchDifficultyName(String id){
        return getName(Constants.CONTENT_DICTIONARY_TYPE_DIFFICULTY, id);
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

    /**
     * Get Knowledge Info
     */
    public List<ContentKnowledgeInfoBO> contentKnowledge(String gradeCode, String subjectCode, String materialVersion){
        if (StringUtils.isBlank(subjectCode) || StringUtils.isBlank(materialVersion)) return null;
        StringBuilder sb = new StringBuilder("com:enableets:edu:package:ppr:paper:knowledge:");
        if (StringUtils.isNotBlank(gradeCode)) sb.append(gradeCode);
        String knowledgeCacheKey = sb.append(gradeCode).append(subjectCode).append(materialVersion).toString();
        String knowledgeStr = stringRedisTemplate.opsForValue().get(knowledgeCacheKey);
        if (StringUtils.isNotBlank(knowledgeStr)){
            return JsonUtils.convert2List(knowledgeStr, ContentKnowledgeInfoBO.class);
        }
        try {
            List<ContentKnowledgeInfoDTO> knowledgeDTOs = contentInfoServiceSDK.getKnowledgeList(gradeCode, subjectCode, materialVersion, Constants.CONTENT_PUBLIC_TYPE, null).getData();
            List<ContentKnowledgeInfoBO> knowledgeInfos = knowledgeDTOs.stream().map(e -> BeanUtils.convert(e, ContentKnowledgeInfoBO.class)).collect(Collectors.toList());
            stringRedisTemplate.opsForValue().set(knowledgeCacheKey, JsonUtils.convert(knowledgeInfos), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            return knowledgeInfos;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<CodeNameMapBO> contentMaterialVersion(String gradeCode, String subjectCode){
        String materialVersionCacheKey = new StringBuilder("com:enableets:edu:package:ppr:paper:materialVersion:").append(StringUtils.isBlank(gradeCode) ? "all" : gradeCode).append("_").append(StringUtils.isBlank(subjectCode) ? "all" : subjectCode).toString();
        String materialVersionStr = stringRedisTemplate.opsForValue().get(materialVersionCacheKey);
        if (StringUtils.isNotBlank(materialVersionStr)){
            return JsonUtils.convert2List(materialVersionStr, CodeNameMapBO.class);
        }else {
            List<ContentMaterialVersionInfoDTO> materialVersions = contentInfoServiceSDK.getMaterialVersionList(gradeCode, subjectCode, Constants.CONTENT_PUBLIC_TYPE).getData();
            if (CollectionUtils.isNotEmpty(materialVersions)){
                List<CodeNameMapBO> materials = materialVersions.stream().map(e -> new CodeNameMapBO(e.getMaterialVersion(), e.getMaterialVersionName(), e.getGradeCode(), e.getSubjectCode())).collect(Collectors.toList());
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
    @Cacheable(value = "com:enableets:edu:package:ppr:paper:school:condition", key = "#schoolId")
    public BaseSearchConditionBO schoolConditionDictionary(String schoolId){
        if (StringUtils.isBlank(schoolId)) return null;
        List<QueryDictionaryInfoDTO> stageInfos = dictionaryInfoV2ServiceSDK.query(schoolId, "02", null, null, null, null, null);
        if (CollectionUtils.isEmpty(stageInfos)) return null;
        BaseSearchConditionBO condition = new BaseSearchConditionBO();
        condition.setStages(stageInfos.stream().map(e -> new StageInfoBO(e.getCode(), e.getName())).collect(Collectors.toList()));
        List<QueryGradeStageResultDTO> gradeStageInfos = gradeInfoV2ServiceSDK.query(schoolId, null, null);
        if (CollectionUtils.isEmpty(gradeStageInfos)) {
            logger.error("Get Base Condition From School is Null, School Id is " + schoolId);
            return condition;
        }
        condition.setGrades(gradeStageInfos.stream().map(e -> new GradeInfoBO(e.getGradeCode(), e.getGradeName(), e.getStageCode())).collect(Collectors.toList()));
        List<QuerySubjectResultDTO> subjectInfos = orgSubjectServiceSDK.queryV2(schoolId, null);
        if (CollectionUtils.isEmpty(subjectInfos)) return condition;
        List<SubjectInfoBO> subjects = new ArrayList<>();
        for (QuerySubjectResultDTO subjectInfo : subjectInfos) {
            if (CollectionUtils.isEmpty(subjectInfo.getItems())){
                for (GradeInfoBO grade : condition.getGrades()) {
                    subjects.add(new SubjectInfoBO(subjectInfo.getSubjectCode(), subjectInfo.getSubjectName(), grade.getGradeCode()));
                }
            }else{
                for (GradeInfoBO gradeBO : condition.getGrades()) {
                    for (QuerySubjectResultDTO.ItemInfoDTO subjectItemInfo : subjectInfo.getItems()) {
                        if (CollectionUtils.isNotEmpty(subjectItemInfo.getGrades())) {
                            for (QuerySubjectResultDTO.GradeInfoDTO grade : subjectItemInfo.getGrades()) {
                                if (grade.getGradeCode().equals(gradeBO.getGradeCode())) {
                                    subjects.add(new SubjectInfoBO(subjectInfo.getSubjectCode(), subjectInfo.getSubjectName(), grade.getGradeCode()));
                                }
                            }
                        }
                    }
                }
            }

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
        String dictionaryKey = new StringBuilder("com:enableets:edu:package:ppr:paper:content:dictionary:type:").append(type).toString();
        String jsonStr = stringRedisTemplate.opsForValue().get(dictionaryKey);
        if (StringUtils.isNotBlank(jsonStr)) return JsonUtils.convert2List(jsonStr, CodeNameMapBO.class);
        List<ContentDictionaryInfoDTO> dictionaries = contentDictionaryInfoServiceSDK.query(Constants.DEFAULT_SCHOOL, type, null, null, null, "0");//Query All
        if (CollectionUtils.isEmpty(dictionaries)) return null;
        List<CodeNameMapBO> dics = dictionaries.stream().map(e -> new CodeNameMapBO(e.getCode(), e.getName())).collect(Collectors.toList());
        stringRedisTemplate.opsForValue().set(dictionaryKey, JsonUtils.convert(dics), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
        return dics;
    }
}
