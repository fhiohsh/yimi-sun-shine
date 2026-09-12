package com.sky.dto;

import lombok.Data;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/27 0:24
 */
@Data
public class OrderAddressDTO {

    private Integer orderId;
    private String orderNumber;
    private Integer userId;
    private String addressInfo;
    private Integer orderStatus;
    private String refundReason;//退款原因
    private String cancelReason;//取消原因
}
