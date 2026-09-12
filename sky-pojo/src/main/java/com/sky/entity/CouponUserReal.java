package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/25 0:02
 */

@TableName("coupon_user")
@Data
public class CouponUserReal {


    public static final Integer VERIFY_UN_USE = 0;
    public static final Integer VERIFY_USED = 1; // 已使用
    public static final Integer COUPON_DATED = 2;//过期

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer couponId;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date useTime;
    private Integer orderId;//对应使用的订单
}
