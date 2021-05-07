package com.enableets.edu.pakage.framework.ppr.test.dao;

import org.apache.ibatis.annotations.Param;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.test.po.TestRecipientInfoPO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/23
 **/
public interface TestRecipientInfoDAO extends BaseDao<TestRecipientInfoPO> {

    List<TestRecipientInfoPO> get(@Param("testId") String testId, @Param("userId") String userId);
}
