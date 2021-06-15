package com.enableets.edu.pakage.manager.ppr.core;

import javax.servlet.http.HttpSession;

import com.enableets.edu.pakage.manager.bo.CodeNameMapBO;
import com.enableets.edu.pakage.manager.mark.core.CodeNameWithRelationMap;
import com.enableets.edu.pakage.manager.util.BeanUtils;
import com.enableets.edu.sdk.content.dto.ContentDictionaryInfoDTO;
import com.enableets.edu.sdk.content.service.IContentDictionaryInfoService;
import com.enableets.edu.sdk.paper.dto.CodeNameMapDTO;
import com.enableets.edu.sdk.paper.dto.SubjectRelationDTO;
import com.enableets.edu.sdk.paper.service.IBaseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.pakage.manager.core.BaseInfoService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/05
 **/
@Service
public class PPRBaseInfoService {

    @Autowired
    private BaseInfoService baseInfoService;

    /** 试卷基础数据接口sdk */
    @Autowired
    private IBaseDataService baseDataServiceSDK;

    /** 字典信息接口sdk */
    @Autowired
    private IContentDictionaryInfoService contentDictionaryInfoServiceSDK;

    public String getTeacherBaseInfo(HttpSession session, String userId){
        String teacherSGSBaseInfoStr = (String)session.getAttribute(PPRConstants.SESSION_KEY_TEACHER_BASE_INFO);
        if (StringUtils.isNotBlank(teacherSGSBaseInfoStr)) {
            return teacherSGSBaseInfoStr;
        }
        Map<String, String> teacherBaseInfo = baseInfoService.getTeacherInfo(userId, Boolean.TRUE);
        if (teacherBaseInfo != null) {
            teacherSGSBaseInfoStr = JsonUtils.convert(teacherBaseInfo);
            session.setAttribute(PPRConstants.SESSION_KEY_TEACHER_BASE_INFO, teacherSGSBaseInfoStr);
        }
        return teacherSGSBaseInfoStr;
    }

    /**
     * 根据学科查询题型信息
     * @param subjectCode 学科编码
     * @return
     */
    public List<CodeNameWithRelationMap> queryQuestionType(String subjectCode) {
        List<CodeNameWithRelationMap> questionTypeList = getQuestionType();
        if (StringUtils.isBlank(subjectCode)) {
            return questionTypeList;
        }
        List<CodeNameWithRelationMap> result = new ArrayList<CodeNameWithRelationMap>();
        for (CodeNameWithRelationMap questype : questionTypeList) {
            if (subjectCode.equals(questype.getRelationCode())) {
                result.add(questype);
            }
        }
        return result;
    }

    /**
     * 查询题型列表
     */
    public List<CodeNameMapBO> queryAllQuestionType(){
        return queryDictionaryByTypeCode(PPRConstants.DICTIONARY_QUESTONTYPE_TYPE_CODE);
    }

    /**
     * 根据字典信息
     * @param typeCode 字典类型代码
     * @return 字典信息
     */
    private List<CodeNameMapBO> queryDictionaryByTypeCode(String typeCode) {
        List<ContentDictionaryInfoDTO> result = contentDictionaryInfoServiceSDK.query(PPRConstants.DEFAULT_SCHOOL_ID, typeCode, null, null, null, PPRConstants.DICTIONARY_MENU_LEVEL_ROOT);
        if (CollectionUtils.isEmpty(result)) {
            return Collections.emptyList();
        }
        return BeanUtils.convert(result, CodeNameMapBO.class);
    }


    /**
     * 查询题型信息
     *
     * @return
     */
    @Cacheable(value = "com:enableets:edu:package:questiontype", key = "'all'")
    public List<CodeNameWithRelationMap> getQuestionType() {
        List<SubjectRelationDTO> subjectRelations = baseDataServiceSDK.querySubjectRelation(null);
        if (CollectionUtils.isEmpty(subjectRelations)) {
            return Collections.emptyList();
        }

        List<CodeNameWithRelationMap> result = new ArrayList<CodeNameWithRelationMap>();
        for (SubjectRelationDTO relation : subjectRelations) {
            if (!CollectionUtils.isEmpty(relation.getQuestionTypeList())) {
                for (CodeNameMapDTO dto : relation.getQuestionTypeList()) {
                    result.add(new CodeNameWithRelationMap(dto.getCode(), dto.getName(), relation.getSubjectCode()));
                }
            }
        }
        return result;
    }
}
