package com.sky.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/21 16:48
 */
@Data
public class ShopCartVO {
    private Integer id;
    private Integer quantity;
    private Integer productId;
    private String productName;
    private String attrInfo;
    private String productImage;
    private BigDecimal productPrice;
}
