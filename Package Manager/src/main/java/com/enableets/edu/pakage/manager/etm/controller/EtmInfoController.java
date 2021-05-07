package com.enableets.edu.pakage.manager.etm.controller;


import com.enableets.edu.framework.core.controller.OperationResult;
import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.pakage.manager.core.BaseInfoService;
import com.enableets.edu.pakage.manager.etm.bo.EtmBookInfoBO;
import com.enableets.edu.pakage.manager.etm.bo.EtmBookPageBO;
import com.enableets.edu.pakage.manager.etm.service.ETMDictionaryInfoService;
import com.enableets.edu.pakage.manager.etm.service.EtmInfoService;
import com.enableets.edu.pakage.manager.etm.vo.EtmBookInfoVO;
import com.enableets.edu.pakage.manager.etm.vo.EtmBookPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/manager/package/etm")
public class EtmInfoController {


    private static final String ETM_BOOKINFO_EDITPAGE = "etm/etmbook/edit";

    private static final String ETM_BASEINFO_PAGE = "etm/etmbook/editBaseInfo";


    @Autowired
    private EtmInfoService etmInfoService;

    @Autowired
    private ETMDictionaryInfoService dictionaryInfoService;

    @Autowired
    private BaseInfoService baseInfoService;

    @Value("${storage.host.upload-url}")
    private String uploadUrl;

    /**
     * edit etm
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping("/etmEdit")
    public String etmEdit(@RequestParam(value = "userId") String userId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("uploadUrl", uploadUrl);
        return ETM_BOOKINFO_EDITPAGE;
    }

    /**
     * edit baseInfo
     * @param book
     * @param model
     * @return
     */
    @RequestMapping(value = "/baseinfo/preedit")
    public String baseInfo(@ModelAttribute(name = "book") EtmBookInfoVO book, Model model) {
        model.addAttribute("stageGradeSubjects", dictionaryInfoService.schoolConditionDictionary(baseInfoService.getUserSchoolInfo(book.getUserId()).getId()));
        model.addAttribute("termCode", book.getTermCode());
        model.addAttribute("textBookVersionCode", book.getTextBookVersionCode());
        model.addAttribute("name", book.getTextBookName());
        model.addAttribute("isbn", book.getIsbn());
        model.addAttribute("coverId", book.getCoverId());
        model.addAttribute("coverUrl", book.getCoverUrl());
        model.addAttribute("coverImgName", book.getCoverImgName());
        model.addAttribute("uploadUrl",uploadUrl);
        return ETM_BASEINFO_PAGE;
    }


    /**
     * edit one page
     * @param etmBookPageVO
     * @return
     */
    @RequestMapping(value = "/editOnePage", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editOnePage(@RequestBody EtmBookPageVO etmBookPageVO) {
        EtmBookInfoBO etmBookInfoBO = etmInfoService.edit(BeanUtils.convert(etmBookPageVO, EtmBookPageBO.class));
        return new OperationResult(BeanUtils.convert(etmBookInfoBO,EtmBookInfoVO.class));
    }

    /**
     * detele one page
     * @param pageInfoId
     * @return
     */
    @RequestMapping(value = "/deleteOnePage", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult deleteOnePage(@RequestBody String pageInfoId) {
        Boolean result = etmInfoService.delete(pageInfoId);
        return new OperationResult(result);
    }

    /**
     * add etm book info
     * @param etmBookInfoVO
     * @return
     */
    @RequestMapping(value = "/addEtmBookInfo", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult addEtmBookInfo(@RequestBody EtmBookInfoVO etmBookInfoVO) {
        EtmBookInfoBO etmBookInfoBO = etmInfoService.add(BeanUtils.convert(etmBookInfoVO, EtmBookInfoBO.class));
        return new OperationResult(BeanUtils.convert(etmBookInfoBO,EtmBookInfoVO.class));
    }

    /**
     *  edit etm book info
     * @param contentId
     * @param model
     * @return
     */
    @RequestMapping(value = "/parseEtmBookInfo",method = RequestMethod.GET)
    public String editEtmBookInfo(@RequestParam(value = "contentId")Long contentId, Model model) {
        EtmBookInfoBO etmBookInfoBO = etmInfoService.parse(contentId);
        EtmBookInfoVO etmBookInfoVO = BeanUtils.convert(etmBookInfoBO, EtmBookInfoVO.class);
        model.addAttribute("etmBookInfo", etmBookInfoVO);
        model.addAttribute("userId",etmBookInfoVO.getUserId());
        model.addAttribute("uploadUrl", uploadUrl);
        return ETM_BOOKINFO_EDITPAGE;
    }


    /**
     *  edit etm book info
     * @param bookId
     * @param model
     * @return
     */
    @RequestMapping(value = "/queryEtmBookInfo",method = RequestMethod.GET)
    public String queryEtmBookInfo(@RequestParam(value = "bookId")String bookId, Model model) {
        EtmBookInfoBO etmBookInfoBO = etmInfoService.query(bookId);
        EtmBookInfoVO etmBookInfoVO = BeanUtils.convert(etmBookInfoBO, EtmBookInfoVO.class);
        model.addAttribute("etmBookInfo", etmBookInfoVO);
        model.addAttribute("userId",etmBookInfoVO.getUserId());
        model.addAttribute("uploadUrl", uploadUrl);
        return ETM_BOOKINFO_EDITPAGE;
    }

}



