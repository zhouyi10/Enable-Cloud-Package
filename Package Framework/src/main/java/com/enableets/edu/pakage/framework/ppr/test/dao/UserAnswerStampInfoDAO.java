package com.enableets.edu.pakage.framework.ppr.test.dao;

import org.apache.ibatis.annotations.Param;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.test.po.TAsUserAnswerStampPO;
import com.enableets.edu.pakage.framework.ppr.test.po.UserAnswerStampInfoPO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/30
 **/
public interface UserAnswerStampInfoDAO extends BaseDao<UserAnswerStampInfoPO> {

    public List<TAsUserAnswerStampPO> getUserAnswerStamps(@Param("testUserId") String testUserId);
}
