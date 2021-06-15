package com.enableets.edu.pakage.framework.core;

import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.SpringBeanUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/01/28
 **/
@Slf4j
@Data
public abstract class JedisCacheAbstract<T> implements IJedisCache<T> {

    private StringJedisRedisTemplate stringJedisRedisTemplate;

    private String nameSpace;
    private String key;
    private int expireTime;
    private String executeKey;

    public JedisCacheAbstract(){
        stringJedisRedisTemplate = SpringBeanUtils.getBean(StringJedisRedisTemplate.class);
        executeKey = getNameSpace() + (StringUtils.isBlank(getKey()) ? "" : getKey());
        expireTime = getExpireTime();
    }

    public JedisCacheAbstract(StringJedisRedisTemplate stringJedisRedisTemplate) {
        this.stringJedisRedisTemplate = stringJedisRedisTemplate;
        executeKey = getNameSpace() + (StringUtils.isBlank(getKey()) ? "" : getKey());
        expireTime = getExpireTime();
    }

    public abstract String getNameSpace();
    public abstract int getExpireTime();

    @Override
    public void push(List<T> list) {
        log.info("JedisCache push size:{}", CollectionUtils.isEmpty(list) ? 0 : list.size());
        if (CollectionUtils.isNotEmpty(list)){
            List<String> collect = list.stream().map(e -> JsonUtils.convert(e)).collect(Collectors.toList());
           stringJedisRedisTemplate.leftPushAll(executeKey, collect);
        }
    }

    @Override
    public List<T> poll(int size) {
        List<T> list = new ArrayList<>();
        log.info("JedisCache poll size:{}, start bRPop await", size);
        String value = stringJedisRedisTemplate.brpop(executeKey, 10L, TimeUnit.MINUTES); //第一条是空就阻塞10分钟
        if (StringUtils.isBlank(value)) return null;
        log.info("JedisCache poll bRPop value succeed");
        list.add(JsonUtils.convert(value, (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]));
        for (int i = 1; i < size; i++) {
            value = stringJedisRedisTemplate.rightPop(executeKey);
            if (StringUtils.isBlank(value)) break;
            list.add(JsonUtils.convert(value, (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]));
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0 || this.size() == 0 ? true : false;
    }

    @Override
    public int size(){
        Long size = stringJedisRedisTemplate.size(executeKey);
        return size == null ? 0 : size.intValue();
    }

    @Override
    public void empty(){
        stringJedisRedisTemplate.delete(executeKey);
    }

    @Override
    public void put(String key, String value) {
        stringJedisRedisTemplate.set(nameSpace + key, value, expireTime);
    }

    @Override
    public String get(String key) {
        return stringJedisRedisTemplate.get(nameSpace + key);
    }

    @Override
    public void del(String key) {
        stringJedisRedisTemplate.delete(nameSpace + key);
    }
}
