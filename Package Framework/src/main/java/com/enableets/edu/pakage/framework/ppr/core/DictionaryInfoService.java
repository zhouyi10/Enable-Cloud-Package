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

    /** Content SDK Client*/
    @Autowired
    private IContentDictionaryInfoService contentDictionaryInfoServiceSDK;

    /**
     * Get Material Version Name By Code
     * @param code
     * @return
     */
    public String matchMaterialVersionName(String code){
        return getName(Constants.CONTENT_DICTIONARY_TYPE_MATERIAL_VERSION, code);
    }

    /**
     * Get Question Type Name By Id
     */
    public String matchQuestionTypeName(String id){
        return getName(Constants.CONTENT_DICTIONARY_TYPE_QUESTION_TYPE, id);
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
     * Get Base Info From Content
     * @param type Base Info Type
     * @return
     */
    @Cacheable(value = PPRConstants.PACKAGE_PPR_CACHE_KEY_PREFIX + "paper:content:dictionary:type:", key = "#type", unless = "#result == null")
    public List<CodeNameMapBO> contentDictionary(String type){
        List<ContentDictionaryInfoDTO> dictionaries = contentDictionaryInfoServiceSDK.query(Constants.DEFAULT_SCHOOL, type, null, null, null, "0");//Query All
        if (CollectionUtils.isEmpty(dictionaries)) return null;
        return dictionaries.stream().map(e -> new CodeNameMapBO(e.getCode(), e.getName())).collect(Collectors.toList());
    }
}
