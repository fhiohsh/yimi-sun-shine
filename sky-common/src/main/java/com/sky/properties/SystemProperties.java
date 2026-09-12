package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: Joey
 * @Description:
 * @date:2024/8/16 0:24
 */
@Component
@ConfigurationProperties(prefix = "system.user")
public class SystemProperties {
    private String authkey;
    private String time;


    public String getAuthkey() {
        return authkey;
    }

    public void setAuthkey(String authkey) {
        this.authkey = authkey;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }




}
