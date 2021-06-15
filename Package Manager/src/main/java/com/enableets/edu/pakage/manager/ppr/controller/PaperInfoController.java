package com.enableets.edu.pakage.manager.ppr.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import cn.hutool.core.io.FileUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.enableets.edu.pakage.framework.ppr.bo.BaseSearchConditionBO;
import com.enableets.edu.pakage.framework.ppr.bo.GradeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.StageInfoBO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamStypeInfoPO;
import com.enableets.edu.pakage.manager.ppr.service.ExamStypeInfoService;
import com.enableets.edu.framework.core.controller.OperationResult;
import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.pakage.manager.core.BaseInfoService;
import com.enableets.edu.pakage.manager.core.Constants;
import com.enableets.edu.pakage.manager.core.PackageConfigReader;
import com.enableets.edu.pakage.manager.ppr.bo.*;
import com.enableets.edu.pakage.manager.ppr.core.PPRBaseInfoService;
import com.enableets.edu.pakage.manager.ppr.service.*;
import com.enableets.edu.pakage.manager.ppr.vo.*;
import com.enableets.edu.sdk.paper.service.IPaperInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.io.*;
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

    @Autowired
    private ExamStypeInfoService examStypeInfoService;

    @Autowired
    private IPaperInfoService paperInfoServiceV1;

    @Autowired
    private AnswerCardService answerCardService;

    /** model key */
    public static final String MODEL_KEY_PAPER_INFO = "paperInfo";
    public static final String MODEL_KEY_ANSWER_CARD_INFO = "answerCardInfo";
    public static final String MODEL_KEY_USER_ID = "userId";

    /**
     * Mark & Edit Paper
     * @param paper Paper Info
     * @parame model Model
     * @return
     */
    @RequestMapping(value = "/preedit")
    public String preEdit(@ModelAttribute("paper") PaperInfoVO paper, Model model, HttpSession session){
        if (StringUtils.isNotBlank(paper.getPaperId())){
            PaperInfoBO paperInfo = paperInfoService.get(Long.valueOf(paper.getPaperId()));
            String userId = paper.getUserId();
            BeanUtils.convert(paperInfo, paper);
            paper.setUserId(userId);
            BaseSearchConditionBO stageGradeSubjects = dictionaryInfoService.schoolConditionDictionary(baseInfoService.getUserSchoolInfo(paper.getUserId()).getId());
            paper = addStageToPaper(paper,  stageGradeSubjects);
        }else{
            this.initPaperBaseInfo(paper);
            pprBaseInfoService.getTeacherBaseInfo(session, paper.getUserId());
        }
        ExamStypeInfoPO examStypeinfoPO =null;
        if (StringUtils.isNotBlank(paper.getPaperId())){
            examStypeinfoPO = examStypeInfoService.querybyid(paper.getPaperId());
        }else {
            examStypeinfoPO = new ExamStypeInfoPO();
        }
        model.addAttribute("examStypeinfoPO", examStypeinfoPO);
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

    @RequestMapping(value = "/edit")
    @ResponseBody
    public OperationResult edit(@RequestBody PaperInfoVO paper){
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
        BaseSearchConditionBO stageGradeSubjects = dictionaryInfoService.schoolConditionDictionary(baseInfoService.getUserSchoolInfo(paper.getUserId()).getId());
        paper = addStageToPaper(paper,  stageGradeSubjects);
        model.addAttribute("stageGradeSubjects", stageGradeSubjects);
        return "ppr/paper/editBaseInfo";
    }



    private PaperInfoVO addStageToPaper(PaperInfoVO paper,BaseSearchConditionBO stageGradeSubjects){
        if (paper!=null&&StringUtils.isBlank(paper.getStageCode())){
            if (paper.getGradeCode()!=null){
                List<StageInfoBO> stages = stageGradeSubjects.getStages();
                List<GradeInfoBO> grades = stageGradeSubjects.getGrades();
                for (GradeInfoBO gradeInfoBO :grades){
                    if (gradeInfoBO.getGradeCode().equals(paper.getGradeCode())){
                        paper.setStageCode(gradeInfoBO.getStageCode());
                        for (StageInfoBO stageInfoBO :stages){
                            if (stageInfoBO.getStageCode().equals(gradeInfoBO.getStageCode())){
                                paper.setStageName(stageInfoBO.getStageName());
                            }
                        }
                    }
                }
            }
        }
        return paper;
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
        model.addAttribute("stageGradeSubjects", dictionaryInfoService.schoolConditionDictionary(baseInfoService.getUserSchoolInfoNullOfDefault(condition.getUserId()).getId()));
        model.addAttribute("difficultys", dictionaryInfoService.contentDifficulty());
        model.addAttribute("questionTypes", dictionaryInfoService.contentQuestionType());
        model.addAttribute("questionMarkets",condition.getParams());
        model.addAttribute("_cloudResourceEnable",true);
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
        ExamStypeInfoPO examStypeinfoPO = examStypeInfoService.querybyid(String.valueOf(id));
        if (examStypeinfoPO != null){
            model.addAttribute("examStypeinfoPO", examStypeinfoPO);
        }else {
            examStypeinfoPO = new ExamStypeInfoPO();
            model.addAttribute("examStypeinfoPO", examStypeinfoPO);
        }
        if (paperInfoBO != null) {
            PaperInfoVO paperInfo = BeanUtils.convert(paperInfoBO, PaperInfoVO.class);
            model.addAttribute("onlineFileUrl", packageConfigReader.getOnlineFileUrl());
            model.addAttribute("paperInfo", paperInfo);
            model.addAttribute("printPdf", printPdf);
            return "ppr/paper/preview";
        }else{
            return "ppr/paper/resourceNotExist";
        }
    }


    /**
     * Test Paper Preview Page
     * @param
     * @return
     */
    @RequestMapping(value = "/previewPaper")
    public String previewpaper(@RequestParam String paperStr ,Model model, HttpServletResponse response,WebRequest request){
        //String paperStr = request.getParameter("paper").trim();
        response.setHeader(Constants.HEAD_X_FRAME_OPTIONS, Constants.HEAD_ALLOWALL);
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (StringUtils.isNotBlank(paperStr)) {
            JSONObject json = JSONObject.parseObject(paperStr);
            PaperInfoVO paper = JSON.toJavaObject(json,PaperInfoVO.class);
            JSONObject examStypeinfoPOjsonObj = JSONObject.parseObject(paper.getExamStypeinfoPO());
            ExamStypeInfoPO examStypeinfoPO  = JSON.toJavaObject(examStypeinfoPOjsonObj,ExamStypeInfoPO.class);
            model.addAttribute("onlineFileUrl", packageConfigReader.getOnlineFileUrl());
            model.addAttribute("paperInfo",  paper);
            model.addAttribute("examStypeinfoPO", examStypeinfoPO);
            return "ppr/paper/previewPaper";
        }else{
            return "ppr/paper/resourceNotExist";
        }

    }



    //@ResponseBody
    @RequestMapping(value = "/paperTopEdit")
    public String paperTopEdit(Model model, HttpServletResponse response,WebRequest request){
        String examStypeinfoPOStr = request.getParameter("examStypeinfoPO").trim();
        if (StringUtils.isNotBlank(examStypeinfoPOStr)){
            JSONObject json = JSONObject.parseObject(examStypeinfoPOStr);
            ExamStypeInfoPO examStypeInfoPO = JSON.toJavaObject(json,ExamStypeInfoPO.class);
            response.setHeader(Constants.HEAD_X_FRAME_OPTIONS, Constants.HEAD_ALLOWALL);
            response.setHeader("Access-Control-Allow-Origin", "*");
            model.addAttribute("examStypeinfoPO", examStypeInfoPO);
            return "ppr/paper/paperTopEdit";
        }else {
            return "ppr/paper/resourceNotExist";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/word/preview/{id}")
    public void preview(@PathVariable("id") String id, Model model, HttpServletResponse response) throws IOException {
//        PaperInfoBO paperInfoBO = paperInfoService.getV2(Long.valueOf(id));
//        model.addAttribute("paperInfo", paperInfoBO);
        String staticHtmlPath = paperInfoService.createStaticHtml(Long.valueOf(id));
        if (StringUtils.isBlank(staticHtmlPath)) {
            String str = "试卷不存在!";
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(str);
            out.close();
        }else {
            InputStream inputStream = FileUtil.getInputStream(staticHtmlPath);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        }
    }


 /*   @ResponseBody
    @RequestMapping(value = "/exportWord")
    public void exportWord( @RequestBody  String id, Model model, WebRequest request,HttpServletResponse response) throws IOException {
        String staticHtmlPath = paperInfoService.createStaticHtml(Long.valueOf(id));
        if (StringUtils.isBlank(staticHtmlPath)) {
            String str = "试卷不存在!";
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(str);
            out.close();
        }else {
            InputStream inputStream = FileUtil.getInputStream(staticHtmlPath);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            try {
                boolean b = ExportWordUtil.exportWord(staticHtmlPath);
                System.out.println(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    @RequestMapping(value = "/answercard/template/preedit")
    public String answercardTemplatePreedit(String paperId, String userId, boolean isPdf, Model model){
        if (isPdf) {
            model.addAttribute(MODEL_KEY_PAPER_INFO, BeanUtils.convert(paperInfoService.get(Long.parseLong(paperId)), PaperInfoVO.class));
            model.addAttribute(MODEL_KEY_ANSWER_CARD_INFO, BeanUtils.convert(answerCardService.getAnswerCardInfo(Long.parseLong(paperId), userId), AddAnswerCardInfoVO.class));
            model.addAttribute("isPdf", true);
        }
        return "ppr/paper/answerCard/templateEdit";
    }

    @RequestMapping(value = "/answercard/preedit", method = RequestMethod.GET)
    public String answercardEdit(String paperId, String userId,Model model,WebRequest request) {
        model.addAttribute(MODEL_KEY_PAPER_INFO, BeanUtils.convert(paperInfoService.get(Long.parseLong(paperId)), PaperInfoVO.class));
        model.addAttribute(MODEL_KEY_ANSWER_CARD_INFO, BeanUtils.convert(answerCardService.getAnswerCardInfo(Long.parseLong(paperId), userId), AddAnswerCardInfoVO.class));
        model.addAttribute(MODEL_KEY_USER_ID, userId);
        return "ppr/paper/answerCard/edit";
    }

    @RequestMapping(value = "/answercard/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult edit(@RequestBody AddAnswerCardInfoVO param) {
        AnswerCardInfoBO answerCardInfoBO = answerCardService.addAnswerCardInfo(BeanUtils.convert(param, AnswerCardInfoBO.class));
        return new OperationResult(BeanUtils.convert(answerCardInfoBO, AddAnswerCardInfoVO.class));
    }

}
