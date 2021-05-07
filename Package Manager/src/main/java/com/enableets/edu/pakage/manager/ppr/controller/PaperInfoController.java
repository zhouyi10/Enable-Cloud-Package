package com.enableets.edu.pakage.manager.ppr.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enableets.edu.framework.core.controller.OperationResult;
import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.pakage.manager.core.BaseInfoService;
import com.enableets.edu.pakage.manager.core.Constants;
import com.enableets.edu.pakage.manager.core.PackageConfigReader;
import com.enableets.edu.pakage.manager.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.manager.ppr.bo.PaperQuestionBO;
import com.enableets.edu.pakage.manager.ppr.bo.QueryContentBO;
import com.enableets.edu.pakage.manager.ppr.bo.QueryPaperBO;
import com.enableets.edu.pakage.manager.ppr.bo.QueryQuestionBO;
import com.enableets.edu.pakage.manager.ppr.core.PPRBaseInfoService;
import com.enableets.edu.pakage.manager.ppr.service.DictionaryInfoService;
import com.enableets.edu.pakage.manager.ppr.service.PaperInfoService;
import com.enableets.edu.pakage.manager.ppr.service.QuestionInfoService;
import com.enableets.edu.pakage.manager.ppr.vo.ChooseQuestionVO;
import com.enableets.edu.pakage.manager.ppr.vo.PaperInfoVO;
import com.enableets.edu.pakage.manager.ppr.vo.QueryContentVO;
import com.enableets.edu.pakage.manager.ppr.vo.QueryPaperVO;
import com.enableets.edu.pakage.manager.ppr.vo.QueryQuestionInfoVO;

import java.util.List;

/**
 * Paper Service Controller
 * @author walle_yu@enable-ets.com
 * @since 2020/08/07
 **/
@Controller
@RequestMapping(value = "/manager/package/ppr")
public class PaperInfoController {

    @Autowired
    private PaperInfoService paperInfoService;

    @Autowired
    private QuestionInfoService questionInfoService;

    @Autowired
    private DictionaryInfoService dictionaryInfoService;

    /** User Info Service*/
    @Autowired
    private BaseInfoService baseInfoService;

    @Autowired
    private PPRBaseInfoService pprBaseInfoService;

    @Autowired
    private PackageConfigReader packageConfigReader;

    /**
     * Mark & Edit Paper
     * @param paper Paper Info
     * @param model Model
     * @return
     */
    @RequestMapping(value = "/preedit")
    public String preEdit(@ModelAttribute("paper") PaperInfoVO paper, Model model, HttpSession session){
        if (StringUtils.isNotBlank(paper.getPaperId())){
            PaperInfoBO paperInfo = paperInfoService.get(Long.valueOf(paper.getPaperId()));
            BeanUtils.convert(paperInfo, paper);
        }else{
            this.initPaperBaseInfo(paper);
            pprBaseInfoService.getTeacherBaseInfo(session, paper.getUserId());
        }
        model.addAttribute("questionTypes", dictionaryInfoService.contentQuestionType());
        model.addAttribute(Constants.MODEL_KEY_CONTENT_MANAGER_URL, packageConfigReader.getContentManagerUrl());
        return "ppr/paper/edit";
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public OperationResult add(@RequestBody PaperInfoVO paper){
        paper.setMaterialVersion(null);
        PaperInfoBO paperInfoBO = paperInfoService.add(BeanUtils.convert(paper, PaperInfoBO.class));
        return new OperationResult(paperInfoBO.getPaperId().toString());
    }

    @RequestMapping(value = "/content/json/{paperId}")
    @ResponseBody
    public OperationResult getContentJson(@PathVariable Long paperId) {
        String contentJson = paperInfoService.getContentJson(paperId);
        return new OperationResult(contentJson);
    }

    /**
     * Edit Base Info Before Mark Paper
     * @param paper
     * @return
     */
    @RequestMapping(value = "/baseinfo/preedit")
    public String baseInfo(@ModelAttribute(name = "paper") PaperInfoVO paper, Model model){
        model.addAttribute("stageGradeSubjects", dictionaryInfoService.schoolConditionDictionary(baseInfoService.getUserSchoolInfo(paper.getUserId()).getId()));
        return "ppr/paper/editBaseInfo";
    }

    /**
     * Mark Paper : Choose Question about ZK
     */
    @RequestMapping(value = "/question/choose/with/knowledge")
    public String chooseQuestionWithKnowledge(@ModelAttribute(name = "condition") ChooseQuestionVO condition, Model model){
        condition.setIsZK(Boolean.TRUE);
        model.addAttribute("difficultys", dictionaryInfoService.contentDifficulty());
        model.addAttribute("questionTypes", dictionaryInfoService.contentQuestionType());
        return "ppr/paper/chooseQuestion/withChapter";
    }

    /**
     * Mark Paper : Choose Question
     * @return
     */
    @RequestMapping(value = "/question/choose/with/chapter")
    public String chooseQuestionWithChapter(@ModelAttribute(name = "condition") ChooseQuestionVO condition, Model model){
        if (condition == null)
            condition = new ChooseQuestionVO();
        model.addAttribute("stageGradeSubjects", dictionaryInfoService.schoolConditionDictionary(baseInfoService.getUserSchoolInfo(condition.getUserId()).getId()));
        model.addAttribute("difficultys", dictionaryInfoService.contentDifficulty());
        model.addAttribute("questionTypes", dictionaryInfoService.contentQuestionType());
        model.addAttribute("questionMarkets",condition.getParams());
        return "ppr/paper/chooseQuestion/withChapter";
    }

    /**
     * Make Paper : Choose Question By Paper
     * @param condition
     * @return
     */
    @RequestMapping(value = "/question/choose/with/paper")
    public String chooseQuestionWithPaper(@ModelAttribute("condition") ChooseQuestionVO condition, Model model){
        model.addAttribute("stageGradeSubjects", dictionaryInfoService.schoolConditionDictionary(baseInfoService.getUserSchoolInfo(condition.getUserId()).getId()));
        model.addAttribute("questionTypes", dictionaryInfoService.contentQuestionType());
        return "ppr/paper/chooseQuestion/withPaper";
    }

    /**
     * 组卷-教辅选题页面
     * @return
     */
    @RequestMapping(value = "/question/choose/with/teachingassistant")
    public String chooseQuestionWithTeachingAssistant(@ModelAttribute("condition") ChooseQuestionVO condition, Model model){
        if (condition == null){
            condition = new ChooseQuestionVO();
        }
        model.addAttribute("stageGradeSubjects", dictionaryInfoService.schoolConditionDictionary(baseInfoService.getUserSchoolInfo(condition.getUserId()).getId()));
        model.addAttribute("questionTypes", dictionaryInfoService.contentQuestionType());
        return "ppr/paper/chooseQuestion/withTeachingAssistant";
    }

    @RequestMapping(value = "/content/query", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult contentQuery(QueryContentVO queryContentVO){
        queryContentVO.setTypeCode("C23");
        return new OperationResult(paperInfoService.queryContent(BeanUtils.convert(queryContentVO, QueryContentBO.class)));
    }

    @RequestMapping(value = "/content/count", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult contentCount(QueryContentVO queryContentVO){
        queryContentVO.setTypeCode("C23");
        return new OperationResult(paperInfoService.countContent(BeanUtils.convert(queryContentVO, QueryContentBO.class)));
    }

    /**
     * Paper Question View
     * @param paperId Paper ID
     * @param ztPaperId Zt Paper ID
     * @param model
     * @return
     */
    @RequestMapping(value = "/question/view")
    public String viewPaperQuestion(String paperId, String ztPaperId, Model model){
        if (paperId != null){
            model.addAttribute("paper", paperInfoService.get(Long.valueOf(paperId)));
        }else{

        }
        return "ppr/paper/chooseQuestion/paperQuestions";
    }

    /**
     * Query Paper
     * @param queryPaperVO
     * @return
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public OperationResult query(QueryPaperVO queryPaperVO){
        List<PaperInfoBO> papers = paperInfoService.query(BeanUtils.convert(queryPaperVO, QueryPaperBO.class));
        return new OperationResult(BeanUtils.convert(papers, QueryPaperVO.class));
    }

    /**
     * Count Paper
     * @param queryPaperVO
     * @return
     */
    @RequestMapping(value = "/count")
    @ResponseBody
    public OperationResult count(QueryPaperVO queryPaperVO){
        Integer count = paperInfoService.count(BeanUtils.convert(queryPaperVO, QueryPaperBO.class));
        return new OperationResult(count);
    }

    /**
     * Query Question
     * @param queryQuestionInfoVO
     * @return
     */
    @RequestMapping(value = "/question/query")
    @ResponseBody
    public OperationResult query(QueryQuestionInfoVO queryQuestionInfoVO){
        List<PaperQuestionBO> questions = questionInfoService.query(BeanUtils.convert(queryQuestionInfoVO, QueryQuestionBO.class));
        return new OperationResult(questions);
    }

    /**
     * Count Question
     * @param queryQuestionInfoVO
     * @return
     */
    @RequestMapping(value = "/question/count")
    @ResponseBody
    public OperationResult count(QueryQuestionInfoVO queryQuestionInfoVO){
        return new OperationResult(questionInfoService.count(BeanUtils.convert(queryQuestionInfoVO, QueryQuestionBO.class)));
    }

    /**
     * Add Question used Time
     * @param questionId
     * @return
     */
    @RequestMapping(value = "/used/times/increase")
    @ResponseBody
    public OperationResult increaseUsedTimes(String questionId){
        paperInfoService.increaseUsedTimes(questionId);
        return new OperationResult(Boolean.TRUE);
    }

    /**
     * Match Name By Code
     * @param paper Paper Info
     */
    private void initPaperBaseInfo(PaperInfoVO paper){
        if (StringUtils.isNotBlank(paper.getStageCode()) && StringUtils.isBlank(paper.getStageName())){
            paper.setGradeName(dictionaryInfoService.matchStageName(paper.getStageCode()));
        }
        if (StringUtils.isNotBlank(paper.getGradeCode()) && StringUtils.isBlank(paper.getGradeName())){
            paper.setGradeName(dictionaryInfoService.matchGradeName(paper.getGradeCode()));
        }
        if (StringUtils.isNotBlank(paper.getSubjectCode()) && StringUtils.isBlank(paper.getSubjectName())){
            paper.setSubjectName(dictionaryInfoService.matchSubjectName(paper.getSubjectCode()));
        }
    }

    /**
     * View the statistics of test paper knowledge points page
     * @param model
     * @return
     */
    @RequestMapping(value = "/knowledge/look")
    public String lookKnowledge(String subjectCode, Model model){
        model.addAttribute("materials", dictionaryInfoService.contentMaterialVersion(null, subjectCode));
        model.addAttribute("difficultys", dictionaryInfoService.contentDifficulty());
        return "ppr/paper/lookKnowledge";
    }

    /**
     * Test Paper Preview Page
     * @param id
     * @return
     */
    @RequestMapping(value = "/preview/{id}")
    public String preview(@PathVariable("id") Long id, boolean printPdf, HttpServletResponse response, Model model){
        response.setHeader(Constants.HEAD_X_FRAME_OPTIONS, Constants.HEAD_ALLOWALL);
        response.setHeader("Access-Control-Allow-Origin", "*");
        PaperInfoBO paperInfoBO = paperInfoService.get(id);
        if (paperInfoBO != null) {
            model.addAttribute("onlineFileUrl", packageConfigReader.getOnlineFileUrl());
            model.addAttribute("paperInfo", BeanUtils.convert(paperInfoBO, PaperInfoVO.class));
            model.addAttribute("printPdf", printPdf);
            return "ppr/paper/preview";
        }else{
            return "ppr/paper/resourceNotExist";
        }
    }
}
