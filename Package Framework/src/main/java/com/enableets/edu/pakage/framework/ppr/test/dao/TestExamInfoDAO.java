package com.enableets.edu.pakage.framework.ppr.test.dao;

import org.apache.ibatis.annotations.Param;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.test.po.ExamInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.QuestionInfoPO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/28
 **/
public interface TestExamInfoDAO extends BaseDao<ExamInfoPO> {

    public ExamInfoPO getExamInfo(@Param(value = "examId") String examId, @Param(value = "contentId") String contentId);

    public List<QuestionInfoPO> getExamQuestions(@Param(value = "examId") String examId, @Param(value = "contentId") String contentId);
}
