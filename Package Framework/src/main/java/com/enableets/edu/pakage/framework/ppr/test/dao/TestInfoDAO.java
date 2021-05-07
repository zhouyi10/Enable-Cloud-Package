package com.enableets.edu.pakage.framework.ppr.test.dao;

import org.apache.ibatis.annotations.Param;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.test.po.TestInfoPO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/23
 **/
public interface TestInfoDAO extends BaseDao<TestInfoPO> {

    public TestInfoPO get(@Param("testId") String testId, @Param("activityId") String activityId, @Param("fileId") String fileId, @Param("examId") String examId);

    public List<TestInfoPO> getByActivityId(@Param("activityId") String activityId);
}
