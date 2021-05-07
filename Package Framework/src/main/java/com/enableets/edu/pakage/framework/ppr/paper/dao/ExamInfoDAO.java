package com.enableets.edu.pakage.framework.ppr.paper.dao;

import org.apache.ibatis.annotations.Param;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.QuestionInfoPO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/07
 **/
public interface ExamInfoDAO extends BaseDao<ExamInfoPO> {

    public ExamInfoPO get(@Param(value = "examId") String examId, @Param(value = "contentId") String contentId);

    public List<QuestionInfoPO> getExamQuestions(@Param(value = "examId") String examId, @Param(value = "contentId") String contentId);

    public void deleteById(@Param(value = "examId") String examId);

}
