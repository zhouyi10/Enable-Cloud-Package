package com.enableets.edu.pakage.framework.core;

import com.enableets.edu.pakage.framework.ppr.test.core.TestConstants;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/02/22
 **/

public class StringJedisCache extends JedisCacheAbstract<String> {

    public StringJedisCache(StringJedisRedisTemplate stringJedisRedisTemplate) {
        super(stringJedisRedisTemplate);
    }

    @Override
    public String getNameSpace() {
        return TestConstants.CACHE_PREFIX;
    }

    @Override
    public int getExpireTime() {
        return TestConstants.CACHE_ONE_WEEK;
    }
}
