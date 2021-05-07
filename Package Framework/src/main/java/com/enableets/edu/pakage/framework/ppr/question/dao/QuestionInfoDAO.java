package com.enableets.edu.pakage.framework.ppr.question.dao;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.question.po.QuestionInfoPO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/17
 **/
public interface QuestionInfoDAO extends BaseDao<QuestionInfoPO> {

    public List<QuestionInfoPO> queryByIds(List<String> list);

    public List<QuestionInfoPO> queryByParentIds(List<String> list);
}
