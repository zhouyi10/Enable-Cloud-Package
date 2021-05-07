package com.enableets.edu.pakage.framework.ppr.paper.dao;

import org.apache.ibatis.annotations.Param;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.paper.po.QuestionInfoPO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/26
 **/
public interface PaperQuestionInfoDAO extends BaseDao<QuestionInfoPO> {

    public void batchDelete(@Param(value = "questionIds") List<String> questionIds);
}
