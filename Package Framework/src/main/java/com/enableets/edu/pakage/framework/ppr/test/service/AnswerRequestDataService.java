package com.enableets.edu.pakage.framework.ppr.test.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.enableets.edu.pakage.core.utils.BeanUtils;
import com.enableets.edu.pakage.core.utils.JsonUtils;
import com.enableets.edu.pakage.framework.core.IJedisCache;
import com.enableets.edu.pakage.framework.ppr.test.dao.AnswerRequestDataDAO;
import com.enableets.edu.pakage.framework.ppr.test.po.AnswerRequestDataPO;
import com.enableets.edu.pakage.framework.ppr.test.service.submitV2.bo.AnswerRequestDataBO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/01/29
 **/
@Service
public class AnswerRequestDataService {

    @Autowired
    public AnswerRequestDataDAO answerRequestDataDAO;

    @Autowired
    @Qualifier("saveAnswerJedisCache")
    public IJedisCache<String> saveAnswerJedisCache;

    public List<AnswerRequestDataBO> query(String status) {
        if (StringUtils.isBlank(status)) return null;
        List<String> statuses = Arrays.asList(status.split(","));
        Map<String, Object> condition = new HashMap<>();
        condition.put("statuses", statuses);
        return BeanUtils.convert(answerRequestDataDAO.query(condition), AnswerRequestDataBO.class);
    }

    public void updateStatusList(List<AnswerRequestDataBO> answerRequestDataList) {
        if (CollectionUtil.isEmpty(answerRequestDataList)) return;
        answerRequestDataDAO.updateList(BeanUtils.convert(answerRequestDataList, AnswerRequestDataPO.class));
    }

    public int update(AnswerRequestDataBO answerRequest) {
        if (answerRequest == null) return 0;
        answerRequest.setUpdateTime(new Date());
        int sum = answerRequestDataDAO.update(BeanUtils.convert(answerRequest, AnswerRequestDataPO.class));
        saveAnswerJedisCache.put(answerRequest.getAnswerRequestId(), JSONUtil.toJsonStr(answerRequest));
        return sum;
    }

    public List<AnswerRequestDataBO> batchInsert(List<AnswerRequestDataBO> list) {
        answerRequestDataDAO.batchInsert(BeanUtils.convert(list, AnswerRequestDataPO.class));
        return list;
    }

    public AnswerRequestDataBO getById(String answerRequestId) {
        String answerRequestDataStr = saveAnswerJedisCache.get(answerRequestId);
        if (StringUtils.isBlank(answerRequestDataStr)) {
            AnswerRequestDataPO answerRequestDataPO = new AnswerRequestDataPO();
            answerRequestDataPO.setAnswerRequestId(answerRequestId);
            return BeanUtils.convert(answerRequestDataDAO.selectOne(answerRequestDataPO), AnswerRequestDataBO.class);
        }
        return JsonUtils.convert(answerRequestDataStr, AnswerRequestDataBO.class);
    }
}
