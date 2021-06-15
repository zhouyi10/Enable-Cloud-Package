package com.enableets.edu.pakage.manager.mark.service;

import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.module.cache.ICache;
import com.enableets.edu.pakage.manager.mark.bo.PaperInfoBO;
import com.enableets.edu.pakage.manager.mark.bo.PaperNodeInfoBO;
import com.enableets.edu.pakage.manager.util.BeanUtils;
import com.enableets.edu.sdk.paper.dto.GetPaperInfoResultDTO;
import com.enableets.edu.sdk.paper.service.IPaperInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 试卷信息service
 * @author walle_yu@enable-ets.com
 * @since 2018/12/12
 */
@Service
public class PaperService {

    private static final Logger logger = LoggerFactory.getLogger(PaperService.class);

    /** 试卷信息接口sdk */
    @Autowired
    private IPaperInfoService paperInfoServiceSDK;

    @Resource(name="testInfoCacheSupport")
    ICache<String> testInfoCache;

    public PaperInfoBO getExam(String examId) {
        if (StringUtils.isEmpty(examId)) {
            return null;
        }
        String key = "PaperInfoByExamId:" + examId;
        String resultStr = testInfoCache.get(key);
        if (StringUtils.isNotBlank(resultStr)) return JsonUtils.convert(resultStr, PaperInfoBO.class);
        GetPaperInfoResultDTO paperInfo = paperInfoServiceSDK.get(Long.parseLong(examId));
        PaperInfoBO paperInfoBO = BeanUtils.convert(paperInfo, PaperInfoBO.class);
        if (paperInfoBO == null || CollectionUtils.isEmpty(paperInfoBO.getNodes())) return null;
        List<PaperNodeInfoBO> children = null;
        for (int i = paperInfoBO.getNodes().size() - 1; i >= 0; i--) {
            PaperNodeInfoBO node = paperInfoBO.getNodes().get(i);
            if (node.getLevel() == 4){
                if (children == null) {
                    children = new ArrayList<PaperNodeInfoBO>();
                }
                children.add(paperInfoBO.getNodes().remove(i));
            }else if (node.getLevel() == 3){
            	if (children != null) {
	                Collections.reverse(children);
	                node.setChildren(children);
	                node.setChildAmount(children.size());
	                children = null;
            	} else {
	                node.setChildAmount(0);
            	}
            }
        }
        testInfoCache.put(key, JsonUtils.convert(paperInfoBO));
        return paperInfoBO;
    }
}
