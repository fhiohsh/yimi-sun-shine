package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Joey
 * @Description:
 * @date:2024/6/1 14:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {
    private Long id;
    private String openid;
    private String userName;
    private String introduction;
    private String token;
    private String avatar;
}
