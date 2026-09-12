package com.sky.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @author: Joey
 * @Description:
 * @date:2024/7/19 16:51
 */

@Configuration
@MapperScan("com.sky.mapper")
public class MybatisPlusConfig implements MetaObjectHandler {
    /**
     * 乐观锁
     * 当要更新一条记录的时候，希望这条记录没有被别人更新
     * 乐观锁实现方式
     * 仅支持 updateById(id) 与 update(entity, wrapper) 方法
     * 在 update(entity, wrapper) 方法下, wrapper 不能复用!!!
     * */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * 自动注入
     * @return
     */
    @Bean
    public ISqlInjector sqlInjector(){
        return new LogicSqlInjector();
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        //设置属性值
        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}
