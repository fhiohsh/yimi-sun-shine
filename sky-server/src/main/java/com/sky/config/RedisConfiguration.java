package com.sky.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author: Joey
 * @Description: Redis配置
 * @date:2024/4/18 16:48
 */
@Configuration
public class RedisConfiguration {
    @Autowired
    private RedisTemplate redisTemplate;
    @Bean
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        //调用redisJsonUtil
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        //打开事务支持
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }
//    @Bean
//    public RedisTemplate<String, Object> stringSerializerRedisTemplate() {
//        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringSerializer);
//        redisTemplate.setValueSerializer(stringSerializer);
//        redisTemplate.setHashKeySerializer(stringSerializer);
//        redisTemplate.setHashValueSerializer(stringSerializer);
//        return redisTemplate;
//    }


}
