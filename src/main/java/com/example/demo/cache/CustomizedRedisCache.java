package com.example.demo.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisOperations;

import com.example.demo.helper.ThreadTaskHelper;
import com.example.demo.utils.SpringUtil;

/**
 * 自定义的redis缓存
 */
@SuppressWarnings("rawtypes")
public class CustomizedRedisCache extends RedisCache {

	private static final Logger logger = LoggerFactory.getLogger(CustomizedRedisCache.class);

	private RedisOperations redisOperations;

	/**
	 * 缓存主动在失效前强制刷新缓存的时间 单位：秒
	 */
	private long preloadSecondTime = 0;

	public CustomizedRedisCache(String name, byte[] prefix,
			RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration,
			long preloadSecondTime) {
		super(name, prefix, redisOperations, expiration);
		this.redisOperations = redisOperations;
		this.preloadSecondTime = preloadSecondTime;
	}

	public CustomizedRedisCache(String name, byte[] prefix,
			RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration,
			long preloadSecondTime, boolean allowNullValues) {
		super(name, prefix, redisOperations, expiration);
		this.redisOperations = redisOperations;
		this.preloadSecondTime = preloadSecondTime;
	}

	@SuppressWarnings("unchecked")
	public ValueWrapper get(final Object key) {

		ValueWrapper valueWrapper = super.get(key);
		if (null != valueWrapper) {
			Long ttl = this.redisOperations.getExpire(key);
			if (null != ttl && ttl <= this.preloadSecondTime) {
				logger.info("key:{} ttl:{} preloadSecondTime:{}", key, ttl, preloadSecondTime);
				ThreadTaskHelper.run(new Runnable() {
					@Override
					public void run() {
						// 重新加载数据
						logger.info("refresh key:{}", key);
						CacheSupport cacheSupportBean =SpringUtil.getBean(CacheSupport.class);
						if (null != cacheSupportBean) {
							cacheSupportBean.refreshCacheByKey(CustomizedRedisCache.super.getName(), key.toString());
						} else {
							logger.info("cacheSupportBean is {}", cacheSupportBean);
						}
					}
				});

			}
		}
		return valueWrapper;
	}
}
