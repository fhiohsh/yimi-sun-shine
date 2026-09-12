package com.sky.controller.request;

import lombok.Data;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/26 1:05
 */
@Data
public class CouponTemplateStatusRequest {

    private Integer templateId;
    private Boolean isActive;
    private Integer actId;
    private Boolean activityStatus;
}
