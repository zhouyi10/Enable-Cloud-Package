package com.enableets.edu.pakage.manager.mark.core;

import com.enableets.edu.module.cache.impl.RedisSupportCache;

import java.io.Serializable;
import java.util.Set;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/27
 **/
public class RedisSupportExtendCache<T extends Serializable> extends RedisSupportCache<T> {

    public RedisSupportExtendCache(){

    }

    private String getStorageKey(String key) {
        return getNameSpace() + key;
    }

    public Set<String> keys(String key){
        return getRedisTemplate().keys(new StringBuilder(getStorageKey(key)).append("*").toString());
    }

    public T get(String key) {
        return getRedisTemplate().opsForValue().get(getStorageKey(key));
    }

    public T getByCompleteKey(String key) {
        return getRedisTemplate().opsForValue().get(key);
    }

    public void remove(String key) {
        getRedisTemplate().delete(this.getStorageKey(key));
    }

    public boolean contains(String key) {
        return getRedisTemplate().hasKey(this.getStorageKey(key));
    }

}
