package com.ys.common.redis.configure;

import java.nio.charset.Charset;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.ys.common.core.constant.Constants;

/**
 * Redis FastJson2 Serializer
 * Custom Redis serializer using FastJson2 for JSON serialization/deserialization
 *
 * @author ruoyi
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T>
{
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter("org.springframework", "com.ys");

    private Class<T> clazz;

    /**
     * Constructor
     *
     * @param clazz Class type for serialization/deserialization
     */
    public FastJson2JsonRedisSerializer(Class<T> clazz)
    {
        super();
        this.clazz = clazz;
    }

    /**
     * Serialize object to byte array
     *
     * @param t Object to serialize
     * @return Serialized byte array
     * @throws SerializationException if serialization fails
     */
    @Override
    public byte[] serialize(T t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }
        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    /**
     * Deserialize byte array to object
     *
     * @param bytes Byte array to deserialize
     * @return Deserialized object
     * @throws SerializationException if deserialization fails
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length <= 0)
        {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return JSON.parseObject(str, clazz, AUTO_TYPE_FILTER);
    }
}
