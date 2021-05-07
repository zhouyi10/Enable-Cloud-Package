package com.enableets.edu.pakage.framework.ppr.test.dao;

import org.apache.ibatis.annotations.Param;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.test.po.UserAnswerInfoPO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/30
 **/
public interface PackageUserAnswerInfoDAO extends BaseDao<UserAnswerInfoPO> {

    public List<UserAnswerInfoPO> queryUserMarkQuestion(@Param("testId") String testId);
}
