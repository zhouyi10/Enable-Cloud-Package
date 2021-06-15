package com.enableets.edu.pakage.manager.mark.controller;


import com.enableets.edu.framework.core.controller.ControllerAdapter;
import com.enableets.edu.pakage.manager.core.PackageConfigReader;
import com.enableets.edu.pakage.manager.mark.bo.MarkAnswerInfoBO;
import com.enableets.edu.pakage.manager.mark.bo.PaperInfoBO;
import com.enableets.edu.pakage.manager.mark.bo.TestInfoBO;
import com.enableets.edu.pakage.manager.mark.core.OperationResult;
import com.enableets.edu.pakage.manager.mark.service.MarkService;
import com.enableets.edu.pakage.manager.mark.service.TestInfoService;
import com.enableets.edu.pakage.manager.util.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 批阅页面
 * @Author walle_yu@enable-ets.com
 * @since 2018/12/13
 */
@Controller
@RequestMapping(value =  "/manager/package/mark")
public class MarkController extends ControllerAdapter<String> {

	private static final Logger logger = LoggerFactory.getLogger(MarkController.class);



	/** model key 试卷信息 */
	public static final String MODEL_KEY_EXAM = "exam";

	/** model key 测验信息 */
	public static final String MODEL_KEY_TEST = "test";

	/** 批阅页面 缩略图url*/
	public static final String PICTURE_URL = "pictureUrl";

	/** 批阅页面 文件预览url*/
	public static final String PREVIEW_FILE_URL = "previewFileUrl";

	public static final String MARK_QUESTION_PAGE = "markV2/markQuestion";

	/**
	 * 批阅错误页面
	 **/
	public static final String MARK_ERROR_PAGE = "markV2/error/error";

	/** 批阅信息service */
    @Autowired
    public MarkService markService;

	@Autowired
	private PackageConfigReader packageConfigReader;

	@Autowired
    private TestInfoService testInfoService;

	@Value("${assessment.config.isCertify:false}")
	private boolean isCertify;


	@ResponseBody
	@RequestMapping(value = "/markStatus/update")
	public void updateMarkStatus(String testId,String loginUserId, String markingCacheInfo){
		markService.updateMarkStatusIntervalV2(testId,loginUserId, markingCacheInfo);
	}

	@ResponseBody
	@RequestMapping(value = "/markStatus/clean")
	public void cleanMarkStatus(String testId,String loginUserId){
		markService.cleanMarkStatusV2(testId,loginUserId);
	}


	/**
	 * 提交批阅结果
	 * @param markInfo
	 * @return
	 */
	@RequestMapping(value = "/domark", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult<Boolean> doMark(@RequestBody MarkAnswerInfoBO markInfo){
        markService.mark(markInfo);
        return new OperationResult<>(Boolean.TRUE);
    }



	/**
	 * 按题批阅页面
	 * @param activityId 活动标识
	 * @param fileId 试卷文件标识
	 * @param testId 考试标识
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/question", method = RequestMethod.GET)
	public String markQuestion(String activityId, String fileId, String testId, String userId, String actionType, String groupIds,String loginUserId, Model model){
        TestInfoBO testInfoBO = testInfoService.get(testId, activityId, fileId);
        boolean flag = markService.checkMarkStatusV2(testInfoBO.getTestId(), testInfoBO.getActivityId(), groupIds, userId,loginUserId, actionType);
		if (flag){
			model.addAttribute("message", MessageUtils.getMessage("MSG_70_03_02_003+-"));
			return MARK_ERROR_PAGE;
		}
		PaperInfoBO exam = markService.getTestExam(testId, activityId, fileId,loginUserId,actionType);
		model.addAttribute(PICTURE_URL, packageConfigReader.getPictureUrl());
		model.addAttribute(PREVIEW_FILE_URL, packageConfigReader.getPreviewFileUrl());
		model.addAttribute("markingCacheInfo", markService.getMarkingCacheInfo(testInfoBO.getTestId(),loginUserId));
		Map<String, Object> resultMap = markService.queryAnswersByQuestionAssignment(testId, userId, groupIds, loginUserId, exam);
		model.addAttribute(MODEL_KEY_TEST, resultMap.get("markAnswerInfo"));
		model.addAttribute(MODEL_KEY_EXAM, resultMap.get("exam"));
		model.addAttribute("testId", testInfoBO.getTestId());
		model.addAttribute("groupId", groupIds);
		model.addAttribute("activityId", activityId);
		model.addAttribute("fileId", fileId);
		model.addAttribute("userId", userId);
		model.addAttribute("loginUserId", loginUserId);
		model.addAttribute("isCertify", isCertify);
		model.addAttribute("actionType", actionType);
		return MARK_QUESTION_PAGE;
	}


}
