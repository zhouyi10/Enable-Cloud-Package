package com.enableets.edu.pakage.framework.ppr.test.service;

import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.pakage.framework.core.Constants;
import com.enableets.edu.pakage.framework.ppr.test.dao.TestExamInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.po.ExamInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.QuestionInfoPO;
import com.enableets.edu.pakage.framework.ppr.bo.ExamInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionInfoBO;
import com.enableets.edu.pakage.framework.ppr.utils.PaperInfoUtils;
import com.enableets.edu.sdk.content.dto.ContentInfoDTO;
import com.enableets.edu.sdk.content.service.IContentInfoService;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/28
 **/
@Service
public class TestPaperService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TestExamInfoDAO testExamInfoDAO;

    @Autowired
    private IContentInfoService contentInfoServiceSDK;

    public PaperInfoBO get(String paperId){
        if (StringUtils.isBlank(paperId)) return null;
        String redisKey = new StringBuilder(PPRConstants.PACKAGE_PPR_CACHE_KEY_PREFIX).append("paper:").append(paperId).toString();
        String paperStr = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(paperStr)){
            return JsonUtils.convert(paperStr, PaperInfoBO.class);
        }else{
            ExamInfoPO examInfo = testExamInfoDAO.getExamInfo(paperId, null);
            if (examInfo == null) return null;
            List<QuestionInfoPO> examQuestions = testExamInfoDAO.getExamQuestions(paperId, null);
            ExamInfoBO exam = PaperInfoUtils.mergeExamAndQuestion(BeanUtils.convert(examInfo, ExamInfoBO.class), BeanUtils.convert(examQuestions, QuestionInfoBO.class));
            PaperInfoBO paperInfoBO = PaperInfoUtils.transformExam2Paper(exam);
            stringRedisTemplate.opsForValue().set(redisKey, JsonUtils.convert(paperInfoBO), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            return paperInfoBO;
        }
    }

    public ContentInfoDTO getContent(Long contentId) {
        return contentInfoServiceSDK.get(contentId).getData();
    }
}
