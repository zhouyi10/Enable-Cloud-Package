package com.enableets.edu.pakage.core.core.cache;

import com.enableets.edu.pakage.core.core.cache.Exception.PackageLocaleCacheException;
import com.enableets.edu.pakage.core.utils.JsonUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/28
 **/
public class LocalCache {

    static {
        Thread t = new Thread(() -> {
            removeOverTimeKey();
        });
        t.setDaemon(true);
        t.start();
    }

    private static Map<String, CacheItem> dataMap = new ConcurrentHashMap<>();

    private static DelayQueue<DelayedItem> queue = new DelayQueue<DelayedItem>();

    private final static Long DEFAULT_LIVING_TIME = 30 * 60 * 1000L;  //最大存在时间

    private final static Integer MAX_CACHE_SIZE = 500;

    public static void put(String key, Object data, Long liveTime) {
        if (liveTime == null) liveTime = DEFAULT_LIVING_TIME;
        remove(key);
        if (dataMap.size() >= MAX_CACHE_SIZE){
            throw new PackageLocaleCacheException("The local cache has reached the maximum limit~");
        }
        DelayedItem item = new DelayedItem(key, liveTime);
        dataMap.put(key, new CacheItem(JsonUtils.convert(data), item));
        queue.put(item);
    }

    public static String get(String key) {
        CacheItem cacheItem = dataMap.get(key);
        if (cacheItem == null) return null;
        return cacheItem.getData();
    }

    public static boolean contains(String key) {
        return dataMap.containsKey(key);
    }

    public static void remove(String key) {
        CacheItem cacheItem = dataMap.get(key);
        if (cacheItem != null){
            dataMap.remove(key);
            queue.remove(cacheItem.getItem());
        }
    }

    public static int size(){
        return dataMap.size();
    }

    public static void clearAll() {
        dataMap.clear();
        queue.clear();
    }

    private static void removeOverTimeKey() {

        while (true) {
            DelayedItem delayedItem = queue.poll(); //延迟期满后保存时间最长的 Delayed 元素
            if (delayedItem != null) {
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
