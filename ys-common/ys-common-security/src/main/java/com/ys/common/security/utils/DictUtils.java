package com.ys.common.security.utils;

import com.alibaba.fastjson2.JSONArray;
import com.ys.common.core.constant.CacheConstants;
import com.ys.common.core.utils.SpringUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.redis.service.RedisService;
import com.ys.system.api.domain.SysDictData;

import java.util.Collection;
import java.util.List;

/**
 *
 *
 * @author ruoyi
 */
public class DictUtils
{
    /**
     *
     *
     * @param key Parameters key
     * @param dictDatas  DICTIONARY DATA   LIST
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas)
    {
        SpringUtils.getBean(RedisService.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     *
     *
     * @param key Parameters key
     * @return dictDatas  DICTIONARY DATA   LIST
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
     *
     *
     * @param key Dictionary
     */
    public static void removeDictCache(String key)
    {
        SpringUtils.getBean(RedisService.class).deleteObject(getCacheKey(key));
    }

    /**
     * CLEAR Dictionary
     */
    public static void clearDictCache()
    {
        Collection<String> keys = SpringUtils.getBean(RedisService.class).keys(CacheConstants.SYS_DICT_KEY + "*");
        SpringUtils.getBean(RedisService.class).deleteObject(keys);
    }

    /**
     * Set cache key
     *
     * @param configKey Parameters key
     * @return Cache key
     */
    public static String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_DICT_KEY + configKey;
    }
}
