package com.gome.gmp.business.impl;

import javax.annotation.Resource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import redis.Gcache;

import com.gome.framework.logging.Logger;
import com.gome.framework.logging.LoggerFactory;
import com.gome.framework.util.JsonUtil;

/**
 * 基础服务
 */
public class BaseBS {

	private static final Logger logger = LoggerFactory.getLogger(BaseBS.class);

	/** redis缓存 **/
	@Resource(name = "commonGcache")
	protected Gcache redisCache;

	/** ehcache缓存 **/
	@Resource(name = "ehCacheManager")
	private CacheManager ehCacheManager;

	/**
	 * 从缓存中获取对象 优先级：ehcache>gcache
	 * 
	 * @param cacheType
	 * @param cacheKey
	 * @param classType
	 * @return
	 */
	public <T> T getCache(String cacheType, String cacheKey, Class<T> classType) {
		T value = getFromEhCache(cacheType, cacheKey, classType);
		if (null == value) {
			value = getFromGcache(cacheType, cacheKey, classType);
			// redis取到的值放入ehcache
			if (null == value) {
				putEhcache(cacheType, cacheKey, value);
			}
		}
		return value;
	}

	/**
	 * 从ehcache获取缓存
	 * 
	 * @param cacheType
	 * @param cacheKey
	 * @param classType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getFromEhCache(String cacheType, String cacheKey, Class<T> classType) {
		Cache cache = ehCacheManager.getCache(cacheType);
		if (null == cache) {
			if (logger.isDebugEnabled()) {
				logger.debug("NO Cache Type In ehcache");
			}
			return null;
		}
		Element cacheEl = cache.get(cacheKey);
		if (null == cacheEl) {
			return null;
		}
		return (T) cacheEl.getObjectValue();
	}

	/**
	 * 从gcache里面获取数据,String类型直接返回,对象类型对JSON反序列化。
	 * 
	 * @param cacheType
	 * @param cacheKey
	 * @param classType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getFromGcache(String cacheType, String cacheKey, Class<T> classType) {
		String gKey = cacheType + cacheKey;
		String gValue = redisCache.get(gKey);
		if (null == gValue) {
			return null;
		}
		// 类型为字符串直接返回
		if (String.class == classType) {
			return (T) gValue;
		} else {
			// 非字符串类型需要JSON转换
			return JsonUtil.parseJson(gValue, classType);
		}
	}

	/**
	 * 向Ehcache存入缓存
	 * 
	 * @param cacheType
	 * @param cacheKey
	 * @param classType
	 * @return
	 */
	public void putEhcache(String cacheType, String cacheKey, Object cacheValue) {
		// 放入本地缓存
		Cache cache = ehCacheManager.getCache(cacheType);
		if (null == cache) {
			ehCacheManager.addCache(cacheType);
			cache = ehCacheManager.getCache(cacheType);
		}
		// 放入本地缓存
		Element cacheEl = new Element(cacheKey, cacheValue);
		cache.put(cacheEl);
	}

	/**
	 * 向Gcache存入缓存
	 * 
	 * @param cacheType
	 * @param cacheKey
	 * @param classType
	 * @return
	 */
	public void putGcache(String cacheType, String cacheKey, Object cacheValue) {
		String gKey = cacheType + cacheKey;
		String gValue = null;
		// 类型为字符串直接返回
		if (String.class == cacheValue.getClass()) {
			gValue = (String) cacheValue;
		} else {
			// 非字符串类型需要JSON转换
			gValue = JsonUtil.toJson(cacheValue);
		}
		redisCache.set(gKey, gValue);
	}
}
