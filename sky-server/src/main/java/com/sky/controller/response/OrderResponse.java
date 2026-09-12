package com.sky.controller.response;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/14 2:12
 */
@Data
public class OrderResponse {
    private Integer id;
    private String orderNumber;
    private Integer userId;

    private String userPhone;

    private String userName;

    private String addressInfo;

    private String deliveryAddress;

    private String imageOne;

    private Integer addressId;

    private String remark;

    private Integer orderStatus;

    private String paymentMethod;

    private BigDecimal totalAmount;

    private String cancelReason;

    private String refundReason;

    private Integer refundStatus;

    private Integer deliveryStatus;

    private BigDecimal deliveryPrice;
//
//  @ApiModelProperty(notes = "订单详情列表")
//  private List<OrderDetails> orderDetailsList;

//  @ApiModelProperty(notes = "地址簿信息")
//  private AddressBook addressBook;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date deliveryTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date receiveDeliveryTime;

    //order_expire_time
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date orderExpireTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;
}
