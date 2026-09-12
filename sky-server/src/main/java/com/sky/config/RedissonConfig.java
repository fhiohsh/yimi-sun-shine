package com.sky.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author: Joey
 * @Description:
 * @date:2024/7/14 1:25
 */
@Configuration
public class RedissonConfig {
    //服务停止即销毁
    @Bean(destroyMethod="shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("123456");
        config.setCodec(new JsonJacksonCodec());
//        RedissonClient redisson = Redisson.create(config);
        return Redisson.create(config);
    }
}
