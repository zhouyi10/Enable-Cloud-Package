package com.enableets.edu.pakage.framework.ppr.test.dao;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.test.po.AnswerRequestDataPO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/01/29
 **/
public interface AnswerRequestDataDAO extends BaseDao<AnswerRequestDataPO> {

    int updateList(List<AnswerRequestDataPO> answerRequestDataList);

    void batchInsert(List<AnswerRequestDataPO> list);

    int update(AnswerRequestDataPO item);

}
