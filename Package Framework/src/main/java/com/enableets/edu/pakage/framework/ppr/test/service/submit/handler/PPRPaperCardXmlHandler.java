package com.enableets.edu.pakage.framework.ppr.test.service.submit.handler;

import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.framework.ppr.test.service.AnswerInfoService;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.bo.SubmitAttributeBO;
import com.enableets.edu.pakage.ppr.action.PPRPackageLifecycle;
import lombok.extern.slf4j.Slf4j;

/**
 * PPR paperCard submit Handler
 * @author walle_yu@enable-ets.com
 * @since 2020/07/10
 **/
@Slf4j
public class PPRPaperCardXmlHandler implements ISubmitXmlHandler {

    @Override
    public boolean isSupport(String xml) {
//        if (StringUtils.isNotBlank(xml)){
//            if (JSONUtil.isJson(xml)) if (JSONUtil.isJson(xml)) return xml.contains("paper") && xml.contains("paperCard");
//            return xml.contains("<paperCard") && xml.contains("<action") && xml.contains("<answerCard");
//        }
//        return false;
        return true;
    }

    @Override
    public String submit(String xml, SubmitAttributeBO attribute) {
        PPRPackageLifecycle lifecycle = new PPRPackageLifecycle();
        EnableCardPackage cardPackage = lifecycle.parse(xml);
        return SpringBeanUtils.getBean(AnswerInfoService.class).submit(cardPackage, attribute);
    }

}
