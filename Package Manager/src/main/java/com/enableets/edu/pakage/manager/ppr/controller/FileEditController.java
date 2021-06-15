package com.enableets.edu.pakage.manager.ppr.controller;

import com.enableets.edu.pakage.manager.core.BaseInfoService;
import com.enableets.edu.pakage.manager.core.PackageConfigReader;
import com.enableets.edu.pakage.manager.mark.core.OperationResult;
import com.enableets.edu.pakage.manager.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.manager.ppr.core.PPRBaseInfoService;
import com.enableets.edu.pakage.manager.ppr.core.PPRConstants;
import com.enableets.edu.pakage.manager.ppr.service.DictionaryInfoService;
import com.enableets.edu.pakage.manager.ppr.service.PPRInfoService;
import com.enableets.edu.pakage.manager.ppr.vo.PaperInfoVO;
import com.enableets.edu.pakage.manager.ppr.vo.PaperNodeInfoVO;
import com.enableets.edu.pakage.manager.util.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author justice_zhou@enable-ets.com
 * @since 2021/5/26
 **/

@Controller
@RequestMapping(value = "/manager/package/ppr/file")
public class FileEditController {

    /**
     * MODEL_KEY 题型信息
     */
    public static final String MODEL_KEY_QUESTION_TYPES = "questionTypes";

    /**
     * 学段年级学科数据
     */
    public static final String CONDITION_SGS_SELECT = "stageGradeSubjects";

    @Autowired
    private DictionaryInfoService dictionaryInfoService;

    /**
     * User Info Service
     */
    @Autowired
    private BaseInfoService baseInfoService;

    @Autowired
    private PackageConfigReader packageConfigReader;

    @Autowired
    private PPRBaseInfoService pprBaseInfoService;

    @Autowired
    private PPRInfoService pprInfoService;


    @Value("${storage.host.upload-url}")
    private String uploadUrl;

    @RequestMapping(value = "/edit")
    public String index(@ModelAttribute(name = "paper") PaperInfoVO paper, Model model, HttpSession session) {
        // Question type filtering
        model.addAttribute(MODEL_KEY_QUESTION_TYPES, pprBaseInfoService.queryAllQuestionType());
        model.addAttribute(PPRConstants.SESSION_KEY_TEACHER_BASE_INFO, pprBaseInfoService.getTeacherBaseInfo(session, paper.getUserId()));
        model.addAttribute("uploadUrl", uploadUrl);
        model.addAttribute("fileView", packageConfigReader.getPreviewFileSrc());
        return "ppr/file/edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult save(@RequestBody PaperInfoVO paper) {
        PaperInfoBO paperBO = pprInfoService.save(BeanUtils.convert(paper, PaperInfoBO.class));
        return new OperationResult(BeanUtils.convert(paperBO,PaperInfoVO.class));
    }

    /**
     * 编辑试卷基础信息页面
     *
     * @param paper 试卷基础信息
     * @param model model
     * @return 基础信息编辑页面路径
     */
    @RequestMapping(value = "/baseinfo/preedit")
    public String preEditBaseInfo(@ModelAttribute(name = "paper") PaperInfoVO paper, Model model) {
        model.addAttribute(CONDITION_SGS_SELECT, dictionaryInfoService.schoolConditionDictionary(baseInfoService.getUserSchoolInfo(paper.getUserId()).getId()));
        //model.addAttribute("isCertify", isCertify);
        return "ppr/file/editBaseInfo";
    }

    /**
     * 编辑模板弹窗
     *
     * @param param 模板信息
     * @param model model
     */
    @RequestMapping(value = "/template/preedit")
    public String importPaperTemplate(PaperInfoVO param, String curKindNodeSearchCode, Model model) {
        //model.addAttribute("meterialVersions", baseInfo.queryMaterialVersion(param.getGradeCode(), param.getSubjectCode()));
        model.addAttribute(MODEL_KEY_QUESTION_TYPES, pprBaseInfoService.queryQuestionType(param.getSubjectCode()));
        return "ppr/file/editTemplate";
    }
}