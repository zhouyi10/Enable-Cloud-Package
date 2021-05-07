package com.enableets.edu.pakage.manager.ppr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enableets.edu.framework.core.controller.OperationResult;
import com.enableets.edu.pakage.framework.bo.CodeNameMapBO;
import com.enableets.edu.pakage.framework.ppr.bo.ContentKnowledgeInfoBO;
import com.enableets.edu.pakage.manager.ppr.service.DictionaryInfoService;

import java.util.List;

/**
 * Controller For All Base Condition
 * @author walle_yu@enable-ets.com
 * @since 2020/08/10
 **/
@Controller
@RequestMapping(value = "/manager/package/ppr/condition")
public class SearchConditionController {

    @Autowired
    private DictionaryInfoService dictionaryInfoService;

    /**
     * Query MaterialVersion
     * @param gradeCode Grade Code
     * @param subjectCode Subject Code
     * @return
     */
    @RequestMapping(value = "/materialversion/query")
    @ResponseBody
    public OperationResult queryMaterial(String gradeCode, String subjectCode){
        List<CodeNameMapBO> materials = dictionaryInfoService.contentMaterialVersion(gradeCode, subjectCode);
        return new OperationResult(materials);
    }

    /**
     * Query Knowledges
     * @param gradeCode Grade Code
     * @param subjectCode Subject Code
     * @param materialVersion
     * @return
     */
    @RequestMapping(value = "/knowledge/query")
    @ResponseBody
    public OperationResult queryKnowledge(String gradeCode, String subjectCode, String materialVersion){
        List<ContentKnowledgeInfoBO> knowledgeInfos = dictionaryInfoService.contentKnowledge(gradeCode, subjectCode, materialVersion);
        return new OperationResult(knowledgeInfos);
    }

    /**
     * Query Question Types
     * @param subjectCode Subject Code
     * @return
     */
    @RequestMapping(value = "/question/type/query")
    @ResponseBody
    public OperationResult queryQuestionType(String subjectCode){
        List<CodeNameMapBO> codeNameMapBOS = dictionaryInfoService.subjectQuestionType(subjectCode);
        return new OperationResult(codeNameMapBOS);
    }

    /**
     * Query Question Ability
     * @param subjectCode
     * @return
     */
    @RequestMapping(value = "/question/ability/query")
    @ResponseBody
    public OperationResult queryQuestionAbility(String subjectCode){
        List<CodeNameMapBO> codeNameMapBOS = dictionaryInfoService.subjectAbility(subjectCode);
        return new OperationResult(codeNameMapBOS);
    }
}
