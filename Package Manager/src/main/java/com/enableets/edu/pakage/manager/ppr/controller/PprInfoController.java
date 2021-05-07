package com.enableets.edu.pakage.manager.ppr.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.enableets.edu.framework.core.controller.OperationResult;
import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.framework.core.util.UrlRewriteUtils;
import com.enableets.edu.pakage.manager.core.Constants;
import com.enableets.edu.pakage.manager.core.PackageConfigReader;
import com.enableets.edu.pakage.manager.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.manager.ppr.core.PPRBaseInfoService;
import com.enableets.edu.pakage.manager.ppr.service.DictionaryInfoService;
import com.enableets.edu.pakage.manager.ppr.service.PPRInfoService;
import com.enableets.edu.pakage.manager.ppr.vo.PaperInfoVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/13
 **/
@Controller
@RequestMapping(value = "/manager/package/ppr/ppr")
public class PprInfoController {

    @Autowired
    private PackageConfigReader packageConfigReader;

    @Autowired
    private DictionaryInfoService dictionaryInfoService;

    @Autowired
    private PPRInfoService pprInfoService;

    @Autowired
    private PPRBaseInfoService pprBaseInfoService;

    @RequestMapping(value = "/preedit")
    public String preEdit(@ModelAttribute("pprInfo") PaperInfoVO pprInfo, Model model, HttpSession session){
        Assert.hasText(pprInfo.getUserId(), "userId cannot be empty!");
        if (StringUtils.isNotBlank(pprInfo.getPaperId())) {
            PaperInfoBO paperInfoBO = pprInfoService.get(pprInfo.getPaperId());
            if (StringUtils.isBlank(paperInfoBO.getPaperType()) || !paperInfoBO.getPaperType().equals("4")) {   //4: PPR picture
                return "redirect:" + "/manager/package/ppr/preedit?userId=" + pprInfo.getUserId() + "&paperId=" + pprInfo.getPaperId();
            }
            model.addAttribute("pprInfo", BeanUtils.convert(paperInfoBO, PaperInfoVO.class));
        } else {
            this.initPaperBaseInfo(pprInfo);
            pprBaseInfoService.getTeacherBaseInfo(session, pprInfo.getUserId());
        }
        model.addAttribute("difficulties", dictionaryInfoService.contentDifficulty());
        model.addAttribute("uploadFileUrl", packageConfigReader.getUploadFileUrl());
        model.addAttribute(Constants.MODEL_KEY_CONTENT_MANAGER_URL, packageConfigReader.getContentManagerUrl());
        return "ppr/ppr/edit";
    }

    @RequestMapping(value = "/preview")
    public String preview(@ModelAttribute("userId") String userId, @ModelAttribute("paperId") String paperId, Model model, HttpServletResponse response){
        response.setHeader(Constants.HEAD_X_FRAME_OPTIONS, Constants.HEAD_ALLOWALL);
        response.setHeader("Access-Control-Allow-Origin", "*");
        PaperInfoBO paperInfoBO = pprInfoService.get(paperId);
        if (StringUtils.isBlank(paperInfoBO.getPaperType()) || !paperInfoBO.getPaperType().equals("4")) {   //4: PPR picture
            return "redirect:" + "/manager/package/ppr/preview/" + paperId;
        }
        model.addAttribute("mode", "preview");
        model.addAttribute("pprInfo", BeanUtils.convert(paperInfoBO, PaperInfoVO.class));
        return "ppr/ppr/edit";
    }

    @RequestMapping(value = "/card/preedit")
    public String card(@ModelAttribute("userId") String userId){
        return "ppr/ppr/cardinfo";
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public OperationResult add(PaperInfoVO paper){
        PaperInfoBO paperBO = pprInfoService.save(BeanUtils.convert(paper, PaperInfoBO.class));
        return new OperationResult(paperBO.getPaperId());
    }

    /**
     * Match Name By Code
     * @param paper Paper Info
     */
    private void initPaperBaseInfo(PaperInfoVO paper){
        if (org.apache.commons.lang3.StringUtils.isNotBlank(paper.getStageCode()) && org.apache.commons.lang3.StringUtils.isBlank(paper.getStageName())){
            paper.setGradeName(dictionaryInfoService.matchStageName(paper.getStageCode()));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(paper.getGradeCode()) && org.apache.commons.lang3.StringUtils.isBlank(paper.getGradeName())){
            paper.setGradeName(dictionaryInfoService.matchGradeName(paper.getGradeCode()));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(paper.getSubjectCode()) && org.apache.commons.lang3.StringUtils.isBlank(paper.getSubjectName())){
            paper.setSubjectName(dictionaryInfoService.matchSubjectName(paper.getSubjectCode()));
        }
    }
}
