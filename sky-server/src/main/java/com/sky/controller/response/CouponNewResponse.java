package com.sky.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/28 19:11
 */
@Data
public class CouponNewResponse {

    private Integer id;//
    private Integer userId;
    private Integer couponId;
    private String activityName;
    private String templateName;
    private Integer type;       // 1 2 3 满减 折扣 无门槛
    private BigDecimal discountAmount;//优惠金额
    private BigDecimal minSpend;//最低使用金额
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date useTime;
    private Integer orderId;//对应使用的订单
}
