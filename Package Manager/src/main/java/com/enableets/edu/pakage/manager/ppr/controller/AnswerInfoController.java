package com.enableets.edu.pakage.manager.ppr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enableets.edu.framework.core.controller.OperationResult;
import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.pakage.framework.ppr.bo.AnswerCardBO;
import com.enableets.edu.pakage.framework.ppr.core.PPRConfigReader;
import com.enableets.edu.pakage.manager.core.Constants;
import com.enableets.edu.pakage.manager.core.PackageConfigReader;
import com.enableets.edu.pakage.manager.ppr.bo.MarkActionInfoBO;
import com.enableets.edu.pakage.manager.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.manager.ppr.bo.UserAnswerCanvasInfoBO;
import com.enableets.edu.pakage.manager.ppr.service.AnswerInfoService;
import com.enableets.edu.pakage.manager.ppr.service.PPRInfoService;
import com.enableets.edu.pakage.manager.ppr.vo.AnswerCardVO;
import com.enableets.edu.pakage.manager.ppr.vo.BeginExamVO;
import com.enableets.edu.pakage.manager.ppr.vo.MarkInfoVO;
import com.enableets.edu.pakage.manager.ppr.vo.PaperInfoVO;
import com.enableets.edu.pakage.manager.ppr.vo.TestInfoResultVO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryTestInfoResultDTO;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRTestInfoService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Answer Info Controller
 * @author walle_yu@enable-ets.com
 * @since 2020/07/31
 **/
@Controller
@RequestMapping(value = "/manager/package/ppr/exam")
public class AnswerInfoController {

    @Autowired
    private IPPRTestInfoService pprTestInfoServiceSDK;

    @Autowired
    private AnswerInfoService answerInfoService;

    @Autowired
    private PPRConfigReader pprConfigReader;

    @Autowired
    private PackageConfigReader packageConfigReader;

    @Autowired
    private PPRInfoService pprInfoService;

    /**
     * Begin To Answer Paper
     * @param beginExamVO
     * @param model
     * @return
     */
    @RequestMapping(value = "/begin", method = RequestMethod.GET)
    public String begin(@ModelAttribute("condition") BeginExamVO beginExamVO, Model model){
        QueryTestInfoResultDTO testInfo = pprTestInfoServiceSDK.get(beginExamVO.getTestId(), beginExamVO.getStepId(), beginExamVO.getFileId(), beginExamVO.getExamId());
        if (testInfo == null){
            model.addAttribute("message", "Exam information does not exist!");
            return "ppr/answer/error";
        }
        beginExamVO.setStepId(testInfo.getActivityId());
        beginExamVO.setExamId(testInfo.getExamId());
        beginExamVO.setTestId(testInfo.getTestId());
        beginExamVO.setExamId(testInfo.getExamId());
        model.addAttribute("testInfo", JsonUtils.convert(BeanUtils.convert(testInfo, TestInfoResultVO.class)));
        model.addAttribute("uploadFileUrl", packageConfigReader.getUploadFileUrl());
        PaperInfoBO paperInfoBO = pprInfoService.get(testInfo.getExamId());
        if (StringUtils.isNotBlank(paperInfoBO.getPaperType()) && paperInfoBO.getPaperType().equals("4")) {   //4: PPR picture
            model.addAttribute("paperInfo", BeanUtils.convert(paperInfoBO, PaperInfoVO.class));
            return "ppr/answer/ppr/answer";
        }
        return "ppr/answer/answer";
    }

    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public OperationResult get(String examId){
        return new OperationResult(answerInfoService.get(examId));
    }

    /**
     * Waiting Page
     * @param
     * @return
     */
    @RequestMapping(value = "/time")
    @ResponseBody
    public OperationResult getTime() {
        return OperationResult.success(new SimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT).format(Calendar.getInstance().getTime()));
    }

    @ResponseBody
    @RequestMapping(value = "/submit")
    public OperationResult submit(@RequestBody AnswerCardVO answerCardVO){
        answerInfoService.submit2(BeanUtils.convert(answerCardVO, AnswerCardBO.class));
        return new OperationResult(Boolean.TRUE);
    }

    /**
     * Mark Student Answer
     * @return
     */
    @RequestMapping(value = "/mark")
    public String mark(@ModelAttribute("condition") MarkInfoVO markInfoVO, Model model){
        QueryTestInfoResultDTO test = pprTestInfoServiceSDK.get(markInfoVO.getTestId(), markInfoVO.getStepId(), null, markInfoVO.getPaperId());
        PaperInfoBO paper = answerInfoService.get(test.getExamId());
        MarkActionInfoBO markAnswerInfo = answerInfoService.queryAnswer(test.getTestId(), markInfoVO.getUserId(), markInfoVO.getGroupIds());
        model.addAttribute("test", markAnswerInfo);
        model.addAttribute("exam", paper);
        model.addAttribute("pictureUrl", pprConfigReader.getPictureUrl());
        return "ppr/mark/mark";
    }

    /**
     * Submit Mark Result
     */
    @RequestMapping(value = "/mark/save")
    @ResponseBody
    public OperationResult doMark(@RequestBody MarkActionInfoBO markInfo, Model model){
        answerInfoService.mark(markInfo);
        return new OperationResult(Boolean.TRUE);
    }

    @ResponseBody
    @RequestMapping(value = "/mark/canvas/file/update", method = RequestMethod.POST)
    public OperationResult fileUpdate(UserAnswerCanvasInfoBO canvasInfoBO, Model model){
        answerInfoService.editCanvas(canvasInfoBO);
        return new OperationResult(Boolean.TRUE);
    }
}
