package com.sky.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author: Joey
 * @Description:
 * @date:2024/8/16 0:32
 */
@Component
public class TimeChangeHandler {

    @EventListener
    public void handleTimeChange(TimePropertyChangeEvent event) {
        // 这里处理 time 属性变化的逻辑
        String newTime = event.getNewTime();
        // 根据新的时间值进行相应操作，比如更新定时任务的周期
        System.out.println("监听到配置文件修改 事件："+event);
        System.out.println("监听到配置文件修改 新属性："+newTime);
    }

}
