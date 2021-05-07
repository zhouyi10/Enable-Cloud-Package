package com.enableets.edu.pakage.manager.etm.controller;

import com.enableets.edu.framework.core.controller.OperationResult;
import com.enableets.edu.pakage.framework.bo.CodeNameMapBO;
import com.enableets.edu.pakage.manager.etm.service.ETMDictionaryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller For All Base Condition
 * @author walle_yu@enable-ets.com
 * @since 2020/08/10
 **/
@Controller
@RequestMapping(value = "/manager/package/etm/condition")
public class ETMSearchConditionController {

    @Autowired
    private ETMDictionaryInfoService dictionaryInfoService;

    /**
     * Query MaterialVersion
     * @param gradeCode Grade Code
     * @param subjectCode Subject Code
     * @return
     */
    @RequestMapping(value = "/materialversion/query")
    @ResponseBody
    public OperationResult queryMaterial(String gradeCode, String subjectCode){
        List<CodeNameMapBO> materials = dictionaryInfoService.smsMaterialVersion(gradeCode, subjectCode);
        return new OperationResult(materials);
    }

    @RequestMapping(value = "/term/query")
    @ResponseBody
    public OperationResult queryTerm(){
        List<CodeNameMapBO> terms = dictionaryInfoService.contentTerm();
        return new OperationResult(terms);
    }

}



