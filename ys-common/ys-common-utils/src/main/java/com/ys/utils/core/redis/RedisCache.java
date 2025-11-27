package com.ys.utils.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Spring Redis Utility Class
 * Provides convenient methods for caching and retrieving various data types in Redis
 *
 * @author ys
 **/
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisCache
{
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * Cache basic objects such as Integer, String, entity classes, etc.
     *
     * @param key Cache key
     * @param value Cache value
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * Cache basic objects with expiration time
     *
     * @param key Cache key
     * @param value Cache value
     * @param timeout Expiration time in seconds
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout)
    {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS );
    }

    /**
     * Cache basic objects with custom expiration time unit
     *
     * @param key Cache key
     * @param value Cache value
     * @param timeout Expiration time
     * @param timeUnit Time unit
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * Set expiration time for a key (in seconds)
     *
     * @param key Redis key
     * @param timeout Timeout value in seconds
     * @return true if successful, false otherwise
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * Set expiration time for a key with custom time unit
     *
     * @param key Redis key
     * @param timeout Timeout value
     * @param unit Time unit
     * @return true if successful, false otherwise
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * Get cached basic object
     *
     * @param key Cache key
     * @return Cached value corresponding to the key
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * Delete a single cached object
     *
     * @param key Cache key to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteObject(final String key)
    {
        return redisTemplate.delete(key);
    }

    /**
     * Delete multiple cached objects
     *
     * @param collection Collection of keys to delete
     * @return Number of keys deleted
     */
    public long deleteObject(final Collection collection)
    {
        return redisTemplate.delete(collection);
    }

    /**
     * Cache List data
     *
     * @param key Cache key
     * @param dataList List data to cache
     * @return Number of elements added to the list
     */
    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * Get cached List object
     *
     * @param key Cache key
     * @return Cached List corresponding to the key
     */
    public <T> List<T> getCacheList(final String key)
    {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * Cache Set data
     *
     * @param key Cache key
     * @param dataSet Set data to cache
     * @return BoundSetOperations for the cached Set
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet)
    {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext())
        {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * Get cached Set
     *
     * @param key Cache key
     * @return Cached Set corresponding to the key
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * Cache Map data
     *
     * @param key Cache key
     * @param dataMap Map data to cache
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * Get cached Map
     *
     * @param key Cache key
     * @return Cached Map corresponding to the key
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * Store data in Hash
     *
     * @param key Redis key
     * @param hKey Hash key
     * @param value Value to store
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * Get data from Hash
     *
     * @param key Redis key
     * @param hKey Hash key
     * @return Object from Hash
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * Get multiple values from Hash
     *
     * @param key Redis key
     * @param hKeys Collection of Hash keys
     * @return List of objects from Hash
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * Get list of cached keys matching a pattern
     *
     * @param pattern String pattern (prefix or wildcard pattern)
     * @return Collection of matching keys
     */
    public Collection<String> keys(final String pattern)
    {
        return redisTemplate.keys(pattern);
    }
}
