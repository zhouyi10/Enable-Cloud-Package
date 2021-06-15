package com.enableets.edu.pakage.framework.core;

import com.enableets.edu.pakage.framework.ppr.test.core.TestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/02/03
 **/

@Configuration
public class JedisRedisConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.lettuce.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.lettuce.pool.max-wait}")
    private long maxWaitMillis;

    @Autowired
    private StringJedisRedisTemplate stringJedisRedisTemplate;

    @Bean("jedisRedisPoolFactory")
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, 50000, password);
        return jedisPool;
    }

    @Bean(name = "testJedisCacheSupport")
    public IJedisCache<String> testJedisCacheSupport() {
        StringJedisCache cache = new StringJedisCache(stringJedisRedisTemplate);
        cache.setNameSpace(TestConstants.CACHE_PREFIX + "test:");
        cache.setExpireTime(60 * 30);
        return cache;
    }

    @Bean(name = "tesRecipientSubmitInfoJedisCache")
    public IJedisCache<String> tesRecipientSubmitInfoJedisCache() {
        StringJedisCache cache = new StringJedisCache(stringJedisRedisTemplate);
        cache.setNameSpace(TestConstants.CACHE_PREFIX + "submit:recipient:");
        return cache;
    }

    @Bean(name = "submitAnswerJedisCache")
    public IJedisCache<String> submitAnswerJedisCache() {
        StringJedisCache cache = new StringJedisCache(stringJedisRedisTemplate);
        cache.setNameSpace(TestConstants.CACHE_PREFIX + "answer:submit:");
        return cache;
    }

    @Bean(name = "saveAnswerJedisCache")
    public IJedisCache<String> saveAnswerJedisCache() {
        StringJedisCache cache = new StringJedisCache(stringJedisRedisTemplate);
        cache.setNameSpace(TestConstants.CACHE_PREFIX + "answer:submit:");
        return cache;
    }



}
