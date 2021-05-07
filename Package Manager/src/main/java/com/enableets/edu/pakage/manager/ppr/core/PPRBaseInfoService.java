package com.enableets.edu.pakage.manager.ppr.core;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.pakage.manager.core.BaseInfoService;

import java.util.Map;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/05
 **/
@Service
public class PPRBaseInfoService {

    @Autowired
    private BaseInfoService baseInfoService;

    public String getTeacherBaseInfo(HttpSession session, String userId){
        String teacherSGSBaseInfoStr = (String)session.getAttribute(PPRConstants.SESSION_KEY_TEACHER_BASE_INFO);
        if (StringUtils.isNotBlank(teacherSGSBaseInfoStr)) {
            return teacherSGSBaseInfoStr;
        }
        Map<String, String> teacherBaseInfo = baseInfoService.getTeacherInfo(userId, Boolean.TRUE);
        if (teacherBaseInfo != null) {
            teacherSGSBaseInfoStr = JsonUtils.convert(teacherBaseInfo);
            session.setAttribute(PPRConstants.SESSION_KEY_TEACHER_BASE_INFO, teacherSGSBaseInfoStr);
        }
        return teacherSGSBaseInfoStr;
    }
}
