package com.ys.common.security.utils;

import java.util.Collection;
import java.util.List;
import com.alibaba.fastjson2.JSONArray;
import com.ys.common.core.constant.CacheConstants;
import com.ys.common.core.utils.SpringUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.redis.service.RedisService;
import com.ys.system.api.domain.SysDictData;

/**
 * Dictionary Utility Class
 * Manages dictionary data caching operations
 *
 * @author ruoyi
 */
public class DictUtils
{
    /**
     * Set dictionary cache
     *
     * @param key Dictionary key
     * @param dictDatas Dictionary data list
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas)
    {
        SpringUtils.getBean(RedisService.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * Get dictionary cache
     *
     * @param key Dictionary key
     * @return Dictionary data list
     */
    public static List<SysDictData> getDictCache(String key)
    {
        JSONArray arrayCache = SpringUtils.getBean(RedisService.class).getCacheObject(getCacheKey(key));
        if (StringUtils.isNotNull(arrayCache))
        {
            return arrayCache.toList(SysDictData.class);
        }
        return null;
    }

    /**
     * Remove specified dictionary cache
     *
     * @param key Dictionary key
     */
    public static void removeDictCache(String key)
    {
        SpringUtils.getBean(RedisService.class).deleteObject(getCacheKey(key));
    }

    /**
     * Clear all dictionary cache
     */
    public static void clearDictCache()
    {
        Collection<String> keys = SpringUtils.getBean(RedisService.class).keys(CacheConstants.SYS_DICT_KEY + "*");
        SpringUtils.getBean(RedisService.class).deleteObject(keys);
    }

    /**
     * Generate cache key for dictionary
     *
     * @param configKey Dictionary key
     * @return Full cache key
     */
    public static String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_DICT_KEY + configKey;
    }
}
