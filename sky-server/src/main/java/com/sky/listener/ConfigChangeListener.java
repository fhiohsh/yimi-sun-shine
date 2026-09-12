package com.sky.listener;

import com.sky.properties.SystemProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author: Joey
 * @Description:
 * @date:2024/8/16 0:28
 */
@Component
public class ConfigChangeListener implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private ApplicationEventPublisher eventPublisher;


    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }


    @EventListener
    public void onYamlConfigChange(SystemProperties configProperties) {
        // 在这里可以检测属性的变化并进行相应处理
        SystemProperties previousProperties = applicationContext.getBean(SystemProperties.class);
        if (!previousProperties.getTime().equals(configProperties.getTime())) {
            // 属性发生变化，执行相应逻辑
            System.out.println("监听到属性变化 => system.time[old]="+previousProperties.getTime());
            System.out.println("监听到属性变化 => system.time="+configProperties.getTime());
            eventPublisher.publishEvent(new TimePropertyChangeEvent(this, configProperties.getTime()));
        }
    }
//    @EventListener
//    public void onTimePropertyChange(SystemProperties myConfigProperties) {
//        // 在这里可以检查 time 属性是否发生了变化
//        // 假设之前有一个旧的 time 值存储在某个地方
//        String oldTime = myConfigProperties.getTime();// 获取旧的 time 值的逻辑（比如从成员变量中获取）
//        if (!oldTime.equals(myConfigProperties.getTime())) {
//            // 发布一个自定义事件表示 time 属性发生了变化
//            applicationEventPublisher.publishEvent(new TimePropertyChangeEvent(this, myConfigProperties.getTime()));
//        }
//    }
}
