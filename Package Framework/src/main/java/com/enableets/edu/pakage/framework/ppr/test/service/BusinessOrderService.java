package com.enableets.edu.pakage.framework.ppr.test.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.token.ITokenGenerator;
import com.enableets.edu.pakage.framework.ppr.test.dao.BusinessOrderDAO;
import com.enableets.edu.pakage.framework.ppr.test.po.BusinessOrderPO;
import com.enableets.edu.pakage.framework.ppr.bo.BusinessOrderBO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * business order information
 * @author duffy_ding
 * @since 2019/08/22
 */
@Service
public class BusinessOrderService {

    public static final Logger LOGGER = LoggerFactory.getLogger(BusinessOrderService.class);

    public static final Integer BUSINESS_ORDER_STATUS_FAILED = -1;
    public static final Integer BUSINESS_ORDER_STATUS_DEFAULT = 0;
    public static final Integer BUSINESS_ORDER_STATUS_SUCCESS = 1;

    public static final Integer BUSINESS_ORDER_VERSION_DEFAULT = 0;

    @Autowired
    private BusinessOrderDAO businessOrderDAO;

    @Autowired
    private ITokenGenerator tokenGenerator;

    @Transactional
    public BusinessOrderBO add(BusinessOrderBO bo) {
        // 1. invalidate original unprocessed data
        businessOrderDAO.invalid(bo.getBusinessId(), bo.getType(), BUSINESS_ORDER_STATUS_DEFAULT.toString());
        // 2. Add
        if (StringUtils.isBlank(bo.getOrderId())) {
            bo.setOrderId(tokenGenerator.getToken().toString());
        }
        bo.setCreateTime(Calendar.getInstance().getTime());
        bo.setUpdateTime(bo.getCreateTime());
        businessOrderDAO.insertSelective(BeanUtils.convert(bo, BusinessOrderPO.class));
        return bo;
    }

    @Transactional
    public List<BusinessOrderBO> batchAdd(List<BusinessOrderBO> bos, String type) {
        // 1. invalidate original unprocessed data
        List<String> businessIds = bos.stream().map(BusinessOrderBO::getBusinessId).collect(Collectors.toList());
        List<String> orderIds = businessOrderDAO.getOrderIds(null, businessIds, type, BUSINESS_ORDER_STATUS_DEFAULT);
        if (CollectionUtils.isNotEmpty(orderIds)) {
            businessOrderDAO.batchInvalidByOrderIds(orderIds);
        }
        Date time = Calendar.getInstance().getTime();
        for (BusinessOrderBO bo : bos) {
            if (StringUtils.isBlank(bo.getOrderId())) {
                bo.setOrderId(tokenGenerator.getToken().toString());
            }
            bo.setCreateTime(time);
            bo.setUpdateTime(bo.getCreateTime());
        }
        businessOrderDAO.batchInsert(BeanUtils.convert(bos, BusinessOrderPO.class));
        return bos;
    }

    /**
     * build business info po
     * @param businessId business id
     * @param type type
     * @param originData origin data
     * @return business info obj
     */
    public BusinessOrderBO buildBusinessInfo(String businessId, String type, String originData) {
        BusinessOrderBO po = buildBusinessBO(businessId, type, originData);
        po.setOrderId(tokenGenerator.getToken().toString());
        return po;
    }

    /**
     * build business info po
     * @param businessId business id
     * @param type type
     * @param originData origin data
     * @return business info obj
     */
    public static BusinessOrderBO buildBusinessBO(String businessId, String type, String originData) {
        BusinessOrderBO po = new BusinessOrderBO();
        po.setBusinessId(businessId);
        po.setType(type);
        po.setOriginData(originData);
        po.setStatus(BUSINESS_ORDER_STATUS_DEFAULT);
        po.setVersion(BUSINESS_ORDER_VERSION_DEFAULT);
        po.setRetryTimes(0);
        po.setCreateTime(Calendar.getInstance().getTime());
        po.setUpdateTime(po.getCreateTime());
        return po;
    }

    /**
     * get business info
     * @param businessId business id
     * @param type type
     * @return
     */
    public BusinessOrderBO get(String businessId, String type) {
        BusinessOrderPO po = businessOrderDAO.get(businessId, type);
        if (po == null) {
            return null;
        }
        return BeanUtils.convert(po, BusinessOrderBO.class);
    }

    /**
     * get order information
     * @param orderId order id
     * @return order information
     */
    public BusinessOrderBO get(String orderId) {
        BusinessOrderPO po = businessOrderDAO.selectByPrimaryKey(orderId);
        if (po == null) {
            return null;
        }
        return BeanUtils.convert(po, BusinessOrderBO.class);
    }

    public Map<String, Object> statistics(String businessIdFuzzyStr) {
        return businessOrderDAO.statistics(businessIdFuzzyStr);
    }

    /**
     * query business data of paper submission
     * @param orderIds orderIds
     * @param businessId businessId
     * @param testId testId
     * @param userId userId
     * @param type type
     * @param status status
     * @param offset offset
     * @param rows rows
     * @return handover business data
     */
    public List<BusinessOrderBO> querySubmitAnswerBusiness(List<String> orderIds, String businessId, String testId, String userId, String type, String status, Integer offset, Integer rows) {
        if (StringUtils.isNotBlank(testId) && StringUtils.isNotBlank(userId)) {
            businessId = testId + "_" + userId;
            testId = null;
            userId = null;
        }
        return BeanUtils.convert(businessOrderDAO.query(orderIds, businessId, testId, userId, type, status, offset, rows), BusinessOrderBO.class);
    }

    /**
     * query the number of business data submitted
     * @param orderIds orderIds
     * @param businessId businessId
     * @param testId testId
     * @param userId userId
     * @param type type
     * @param status status
     * @return count
     */
    public Integer countSubmitAnswerBusiness(List<String> orderIds, String businessId, String testId, String userId, String type, String status) {
        if (StringUtils.isNotBlank(testId) && StringUtils.isNotBlank(userId)) {
            businessId = testId + "_" + userId;
            testId = null;
            userId = null;
        }
        return businessOrderDAO.count(orderIds, businessId, testId, userId, type, status);
    }

    /**
     * query data identification that needs to be retried
     * @return business data
     */
    public List<BusinessOrderBO> queryRetryOrder(String type) {
        return BeanUtils.convert(businessOrderDAO.queryRetryOrder(10, type), BusinessOrderBO.class);
    }

    /**
     * query interrupted data identification
     * @return business data
     */
    public List<BusinessOrderBO> queryInterruptOrder(String type) {
        return BeanUtils.convert(businessOrderDAO.queryInterruptOrder(type), BusinessOrderBO.class);
    }

    /**
     * order starts processing
     * @param orderId
     * @return true/false
     */
    @Transactional
    public boolean start(String orderId, String extendAttrs) {
        int updateCount = businessOrderDAO.start(orderId, extendAttrs);
        return updateCount > 0;
    }

    /**
     * order is successful
     * @param orderId order id
     * @return true/false
     */
    @Transactional
    public boolean success(String orderId, String extendAttrs) {
        int updateCount = businessOrderDAO.success(orderId, extendAttrs);
        return updateCount > 0;
    }

    /**
     * order failed
     * @param orderId
     * @param errorCode
     * @param errorMessage
     * @return true/false
     */
    @Transactional
    public boolean fail(String orderId, String errorCode, String errorMessage) {
        int updateCount = businessOrderDAO.fail(orderId, errorCode, errorMessage);
        return updateCount > 0;
    }
}
