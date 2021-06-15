package com.enableets.edu.pakage.framework.core;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/02/03
 **/

@Component
public class StringJedisRedisTemplate {

    @Autowired
    JedisPool jedisRedisPoolFactory;

    //获得jedis资源的方法
    public Jedis getJedis() {
        return jedisRedisPoolFactory.getResource();
    }

    public void leftPushAll(String key, List<String> collect) {
        Jedis jedis = getJedis();
        try {
            jedis.lpush(key, collect.toArray(new String[collect.size()]));
        } finally {
            jedis.close();
        }
    }

    public String brpop(String key, long timeout, TimeUnit unit) {
        Jedis jedis = getJedis();
        int tm = (int) TimeoutUtils.toSeconds(timeout, unit);
        List<String> brpop;
        try {
            brpop = jedis.brpop(tm, key);
        } finally {
            jedis.close();
        }
        if (CollectionUtils.isNotEmpty(brpop)) {
            return brpop.get(1);
        }
        return null;
    }

    public String rightPop(String key) {
        Jedis jedis = getJedis();
        String rpop;
        try {
            rpop = jedis.rpop(key);
        } finally {
            jedis.close();
        }
        return rpop;
    }

    public Long size(String key) {
        Long llen;
        Jedis jedis = getJedis();
        try {
            llen = jedis.llen(key);
        } finally {
            jedis.close();
        }
        return llen;
    }

    public void delete(String key) {
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    public String get(String key) {
        Jedis jedis = getJedis();
        String value;
        try {
            value = jedis.get(key);
        } finally {
            jedis.close();
        }
        return value;
    }

    public void set(String key, String value, int secondsToExpire) {
        Jedis jedis = getJedis();
        try {
            if (jedis.exists(key)) {
                jedis.mset(key, value);
            } else {
                jedis.set(key, value, new SetParams().nx().ex(secondsToExpire));
            }
        } finally {
            jedis.close();
        }
    }

}
