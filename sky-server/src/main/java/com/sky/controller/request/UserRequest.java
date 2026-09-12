package com.sky.controller.request;

import lombok.Data;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/17 10:39
 */
@Data
public class UserRequest {
    private Integer userId;
    private String introduction;
    private String avatar;
    private String userName;
}
