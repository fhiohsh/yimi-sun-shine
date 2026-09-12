package com.sky.dto;

import com.sky.entity.AddressBook;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Joey
 * @Description: 获取订单 接收的参数
 * @date:2024/9/17 1:28
 */
@Data
public class OrderDTO implements Serializable {

    private Integer orderId;
    private String orderNumber;
    private Integer userId;
    private String userName;
    private String userPhone;
    private BigDecimal totalAmount;
    private BigDecimal deliveryPrice;
    private List<GoodsDTO> goodsList;
    private String remark;
    private Integer addressId;
    private String addressInfo;
    private Integer paymentMethod; // 1微信 2其他
    private Integer couponId; //优惠券

}
