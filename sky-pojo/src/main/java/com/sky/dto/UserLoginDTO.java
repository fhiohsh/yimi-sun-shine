package com.sky.dto;

import lombok.Data;

/**
 * @author: Joey
 * @Description: C端用户登录
 * @date:2024/6/1 14:39
 */
@Data
public class UserLoginDTO {
    private String code;

    private String phone;

    private String phoneCode;
}
