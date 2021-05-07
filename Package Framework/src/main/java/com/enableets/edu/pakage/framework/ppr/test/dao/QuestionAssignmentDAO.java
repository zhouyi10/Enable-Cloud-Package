package com.enableets.edu.pakage.framework.ppr.test.dao;

import org.apache.ibatis.annotations.Param;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.test.po.QuestionAssignmentPO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/09/24
 **/
public interface QuestionAssignmentDAO extends BaseDao<QuestionAssignmentPO> {

    List<QuestionAssignmentPO> query(@Param("testId") String testId, @Param("userId") String userId);

    void remove(@Param("testId") String testId);
}
