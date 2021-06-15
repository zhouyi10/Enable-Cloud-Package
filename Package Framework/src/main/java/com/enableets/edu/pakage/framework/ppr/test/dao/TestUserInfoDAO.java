package com.enableets.edu.pakage.framework.ppr.test.dao;

import org.apache.ibatis.annotations.Param;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.test.po.TestUserInfoPO;

import java.util.List;
import java.util.Map;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/30
 **/
public interface TestUserInfoDAO extends BaseDao<TestUserInfoPO> {

    public List<TestUserInfoPO> queryAnswer(Map<String, Object> condition);

    public List<TestUserInfoPO> getByTestId(@Param("testId") String testId);

    public List<TestUserInfoPO> selectByIds(List<String> list);

    public void completeMark(List<String> list);

    public void completeMark2(@Param("testId") String testId, @Param("userId") String userId);

    public void recalculateTotalScore(List<String> list);

    public void removePrevSubmit(String testId, String userId);
}
