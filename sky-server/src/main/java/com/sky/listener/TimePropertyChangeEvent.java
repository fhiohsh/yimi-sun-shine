package com.sky.listener;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author: Joey
 * @Description:
 * @date:2024/8/16 0:30
 */
public class TimePropertyChangeEvent extends ApplicationEvent {

    private String newTime;


    public TimePropertyChangeEvent(Object source, String newTime) {
        super(source);
        this.newTime = newTime;
    }

    public String getNewTime() {
        return newTime;
    }
}
