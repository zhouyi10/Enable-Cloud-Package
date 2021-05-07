package com.enableets.edu.pakage.framework.ppr.test.dao;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.test.po.ActivityAssignerPO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/09/24
 **/
public interface ActivityAssignerDAO extends BaseDao<ActivityAssignerPO> {

    public void batchAdd(List<ActivityAssignerPO> activityAssigners);

    public void deleteActivityAssigner(String activityId);
}
