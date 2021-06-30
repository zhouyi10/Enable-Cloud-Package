package com.enableets.edu.pakage.framework.ppr.test.dao;

import org.apache.ibatis.annotations.Param;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.ppr.test.po.BusinessOrderPO;

import java.util.List;
import java.util.Map;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/22
 **/
public interface BusinessOrderDAO extends BaseDao<BusinessOrderPO> {

    /**
     * statistics business info
     * @param businessId business id [right fuzzy match]
     * @return
     */
    Map<String, Object> statistics(@Param("businessId") String businessId);

    /**
     * query orders
     * @param orderIds orderIds
     * @param businessId businessId
     * @param testId testId
     * @param userId userId
     * @param type type
     * @param status status
     * @param offset offset
     * @param rows rows
     * @return orders
     */
    List<BusinessOrderPO> query(@Param("orderIds") List<String> orderIds, @Param("businessId") String businessId,
                                @Param("testId") String testId, @Param("userId") String userId,
                                @Param("type") String type, @Param("status") String status, @Param("offset") Integer offset, @Param("rows") Integer rows);


    /**
     * count
     * @param orderIds orderIds
     * @param businessId businessId
     * @param testId testId
     * @param userId userId
     * @param type type
     * @param status status
     * @return count
     */
    Integer count(@Param("orderIds") List<String> orderIds, @Param("businessId") String businessId,
                  @Param("testId") String testId, @Param("userId") String userId,
                  @Param("type") String type, @Param("status") String status);

    /**
     * query failed order
     * @param retryTimes max retry type
     * @param type business type
     * @return update count
     */
    List<BusinessOrderPO> queryRetryOrder(@Param("retryTimes") Integer retryTimes, @Param("type") String type);

    /**
     * query interrupt order
     * @param type business type
     * @return update count
     */
    List<BusinessOrderPO> queryInterruptOrder(@Param("type") String type);

    /**
     * get business by business id
     * @param businessId business id
     * @param type type
     * @return business data
     */
    BusinessOrderPO get(@Param("businessId") String businessId, @Param("type") String type);

    /**
     * batch insert
     * @param businesses 业务数据
     * @return count
     */
    int batchInsert(List<BusinessOrderPO> businesses);

    /**
     * invalid not process data
     * @param businessId business id
     * @param businessIds business id list
     * @param type type
     * @param status status
     * @return update count
     */
    List<String> getOrderIds(@Param("businessId") String businessId, @Param("businessIds") List<String> businessIds, @Param("type") String type, @Param("status") Integer status);

    /**
     * batch invalid not process data
     * @param orderIds order id list
     * @return update count
     */
    int batchInvalidByOrderIds(@Param("orderIds") List<String> orderIds);

    /**
     * invalid not process data
     * @param businessId business id
     * @param type type
     * @param status status
     * @return update count
     */
    int invalid(@Param("businessId") String businessId, @Param("type") String type, @Param("status") String status);

    /**
     * batch invalid not process data
     * @param businessIds business id list
     * @param type type
     * @param status status
     * @return update count
     */
    int batchInvalid(@Param("businessIds") List<String> businessIds, @Param("type") String type, @Param("status") Integer status);

    /**
     * start to process the business
     * @param orderId order id
     * @param extendAttrs extend attr
     * @return update count
     */
    int start(@Param("orderId") String orderId, @Param("extendAttrs") String extendAttrs);

    /**
     * fail to process the business
     * @param orderId order id
     * @param errorCode error code
     * @param errorMessage error message
     * @return update count
     */
    int fail(@Param("orderId") String orderId, @Param("errorCode") String errorCode, @Param("errorMessage") String errorMessage);

    /**
     * success to process the business
     * @param orderId order id
     * @return update count
     */
    int success(@Param("orderId") String orderId, @Param("extendAttrs") String extendAttrs);

    BusinessOrderPO getOneBusinessOrder();
}
