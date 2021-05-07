package com.enableets.edu.pakage.framework.ppr.core;

import org.apache.commons.lang3.StringUtils;

import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.pakage.core.core.cache.DelayedItem;
import com.enableets.edu.pakage.framework.ppr.test.service.TestInfoService;
import com.enableets.edu.pakage.framework.ppr.bo.TestRecipientInfoBO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/04
 **/
public class RecipientCacheMap {

    private static Map<String, List<TestRecipientInfoBO>> dataMap = null;

    private static DelayQueue<DelayedItem> queue = new DelayQueue<DelayedItem>();

    private final static Long MAX_LIVING_TIME = 30000L;  //最大存在时间

    public static List<TestRecipientInfoBO> get(String key){
        if (dataMap == null){
            dataMap = new ConcurrentHashMap<>();
            Thread t = new Thread(() -> {
                removeOverTimeKey();
            });
            t.setDaemon(true);
            t.start();
        }
        if (dataMap.get(key) == null){
            TestInfoService testInfoService = SpringBeanUtils.getBean(TestInfoService.class);
            dataMap.put(key, testInfoService.getRecipients(key));
        }
        DelayedItem tmpItem = new DelayedItem(key, MAX_LIVING_TIME);
        queue.remove(tmpItem); //移除队列中上一次的元素,插入新增(实际是更新存在时间)
        queue.put(tmpItem);
        return dataMap.get(key);
    }

    public static TestRecipientInfoBO get(String key, String userId){
        if (StringUtils.isBlank(userId)) return null;
        List<TestRecipientInfoBO> recipients = get(key);
        TestRecipientInfoBO recipient = null;
        for (TestRecipientInfoBO recipientBO : recipients) {
            if (recipientBO.getUserId().equals(userId)) {
                recipient = recipientBO; break;
            }
        }
        return recipient;
    }


    public static void removeOverTimeKey(){
        while(true){
            DelayedItem delayedItem = queue.poll(); //延迟期满后保存时间最长的 Delayed 元素
            if (delayedItem != null){
                dataMap.remove(delayedItem.getKey());
            }
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
