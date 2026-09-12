package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@TableName("order_details")
@EqualsAndHashCode
public class OrderDetails {
  @TableId(value = "id",type = IdType.AUTO)
  private Integer id;
  private Integer orderId;
  private Long productId;
  private String productName;
  private Integer attrId;
  private BigDecimal unitPrice;
  private Integer quantity;
  private BigDecimal totalPrice;
  private String attrInfo;

//  private Goods goods;



}
