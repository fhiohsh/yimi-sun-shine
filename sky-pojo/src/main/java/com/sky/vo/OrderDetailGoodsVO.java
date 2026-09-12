package com.sky.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/23 0:18
 */
@Data
public class OrderDetailGoodsVO {
    private Integer productId;
    private String productName;
    private String attrInfo;
    private Integer num;
    private BigDecimal price;
    private String image;

}
