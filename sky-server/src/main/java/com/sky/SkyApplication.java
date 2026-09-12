package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@EnableCaching //开启缓存
@EnableScheduling //定时处理
@EnableRabbit
@Slf4j
public class SkyApplication {
    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }
    public static void main(String[] args) {
        SpringApplication.run(SkyApplication.class, args);
        log.info("server started");


        Runtime runtime = Runtime.getRuntime();
// 打印JVM的初始内存和最大内存配置
        System.out.println("JVM初始内存大小: " + runtime.totalMemory() / (1024 * 1024) + " MB");
        System.out.println("JVM最大内存大小: " + runtime.maxMemory() / (1024 * 1024) + " MB");
    }
}
