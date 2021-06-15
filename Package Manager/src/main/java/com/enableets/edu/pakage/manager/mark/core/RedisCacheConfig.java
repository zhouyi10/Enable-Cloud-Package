package com.enableets.edu.pakage.manager.mark.core;

import com.enableets.edu.module.cache.ICache;
import com.enableets.edu.module.cache.impl.RedisSupportCache;
import com.enableets.edu.pakage.manager.ppr.core.PPRConstants;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * redis 缓存配置类
 * @author duffy_ding
 * @since 2018/03/15
 */
@Configuration
@EnableCaching
public class RedisCacheConfig {

	@Bean
	public StringRedisSerializer stringRedisSerializer(){
		return new StringRedisSerializer();
	}

	@Bean
	public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer(ObjectMapper objectMapper) {
		GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
		return jackson2JsonRedisSerializer;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 此项必须配置，否则会报java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to XXX
		objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return objectMapper;
	}


	@Bean
	public ICache<String> reportCacheSupport(RedisConnectionFactory factory) {
		StringRedisTemplate template = new StringRedisTemplate(factory);
		template.afterPropertiesSet();
		RedisSupportCache<String> cache = new RedisSupportCache<String>();
		cache.setNameSpace("enableets:package:manager:reportCache:");
		cache.setExpireTime(60 * 30L);
		cache.setRedisTemplate(template);
		return cache;
	}
	
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory factory, GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer, StringRedisSerializer stringRedisSerializer) {
		return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory))
				.cacheDefaults(redisCacheConfiguration(new GenericJackson2JsonRedisSerializer(), stringRedisSerializer))
				.withCacheConfiguration(PPRConstants.PACKAGE_PPR_CACHE_KEY_PREFIX + "report", redisCacheConfiguration(genericJackson2JsonRedisSerializer, stringRedisSerializer).entryTtl(Duration.ofMinutes(2)))
				.build();
	}

	private RedisCacheConfiguration redisCacheConfiguration(GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer, StringRedisSerializer stringRedisSerializer) {
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.serializeValuesWith(RedisSerializationContext.fromSerializer(genericJackson2JsonRedisSerializer).getValueSerializationPair())
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer));
		config.entryTtl(Duration.ofMinutes(30));
		return config;
	}

	@Bean(name = "markCacheSupport")
	public ICache<String> getMarkUserCacheSupport(RedisConnectionFactory factory) {
		StringRedisTemplate template = new StringRedisTemplate(factory);
		template.afterPropertiesSet();
		RedisSupportCache<String> cache = new RedisSupportCache<String>();
		cache.setNameSpace(PPRConstants.PACKAGE_PPR_CACHE_KEY_PREFIX + "markCache:");
		//过期时间设置为10秒
		cache.setExpireTime(15L);
		cache.setRedisTemplate(template);
		return cache;
	}

	@Bean(name = "markTestCacheSupport")
	public RedisSupportExtendCache<String> markTestCacheSupport(RedisConnectionFactory factory){
		StringRedisTemplate template = new StringRedisTemplate(factory);
		template.afterPropertiesSet();
		RedisSupportExtendCache<String> cache = new RedisSupportExtendCache<String>();
		cache.setNameSpace(PPRConstants.PACKAGE_PPR_CACHE_KEY_PREFIX + "markCache:test:");
		//过期时间设置为10秒
		cache.setExpireTime(15L);
		cache.setRedisTemplate(template);
		return cache;
	}

    @Bean(name = "testInfoCacheSupport")
    public ICache<String> testCacheSupport(RedisConnectionFactory factory){
        StringRedisTemplate template = new StringRedisTemplate(factory);
        template.afterPropertiesSet();
        RedisSupportCache<String> cache = new RedisSupportCache<String>();
        cache.setNameSpace(PPRConstants.PACKAGE_PPR_CACHE_KEY_PREFIX + "test");
        cache.setExpireTime(60 * 30L);
        cache.setRedisTemplate(template);

        return cache;
    }
}
