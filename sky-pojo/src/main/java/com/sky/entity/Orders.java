package com.sky.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@TableName("orders")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Orders{

  /**
   * 订单状态：0，未支付。1，已支付。2，待发货。3，已发货。4，已收货、待评价。
   *         5，退款售后中。6，已退款，7，已完成（评价完成后），8，已取消
   *
   *         UNPAID
   *         PAID
   *         CANCELED
   */
  public static final Integer UNPAID = 0;
  public static final Integer PAID = 1;
  public static final Integer TO_BE_SHIPPED = 2;
  public static final Integer SHIPPED = 3;
  public static final Integer TO_BE_EVALUATED = 4;
  public static final Integer REFUND_IN_PROGRESS = 5;
  public static final Integer REFUNDED = 6; //REFUND_SUCCESSFUL = 3
  public static final Integer COMPLETED = 7;
  public static final Integer CANCELED = 8;

  /**
   * 退款状态 1：用户申请 2：商家同意 3，退款成功
   */
  public static final Integer USER_APPLIED_REFUND = 1;
  public static final Integer MERCHANT_APPROVED = 2;
  public static final Integer REFUND_SUCCESSFUL = 3;

  /**
   * 物流状态 1未发货 2已发货 3已收货
   */
  public static final Integer DELIVERY_NOT_SHIPPED = 1;
  public static final Integer DELIVERY_SHIPPED = 2;
  public static final Integer DELIVERY_RECEIVED = 3;

  public static final String DELIVERY_ADDRESS = "四川省成都市锦江区荷花池街道";



  @TableId(value = "id",type = IdType.AUTO)
  private Integer id;

  @Schema(description = "订单编号")
  private String orderNumber;

  @Schema(description = "用户 ID")
  private Integer userId;

  @Schema(description = "用户手机号")
  private String userPhone;

  @Schema(description = "用户姓名")
  private String userName;

  @Schema(description = "地址详情")
  private String addressInfo;

  @Schema(defaultValue = "发货地")
  private String deliveryAddress;

  @Schema(description = "地址 ID")
  private Integer addressId;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "订单状态")
  private Integer orderStatus;

  @Schema(description = "支付方式")
  private String paymentMethod;

  @Schema(description = "订单总金额")
  private BigDecimal totalAmount;

  @Schema(description = "取消原因")
  private String cancelReason;

  @Schema(description = "退款原因")
  private String refundReason;

  @Schema(description = "退款状态")
  private Integer refundStatus;

  @Schema(description = "发货状态")
  private Integer deliveryStatus;

  @Schema(description = "运费")
  private BigDecimal deliveryPrice;
//
//  @ApiModelProperty(notes = "订单详情列表")
//  private List<OrderDetails> orderDetailsList;

//  @ApiModelProperty(notes = "地址簿信息")
//  private AddressBook addressBook;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private Date deliveryTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private Date receiveDeliveryTime;

  //order_expire_time
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
  private Date orderExpireTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

// @AutoFill(OperationType.INSERT)
// @AutoFill(OperationType.UPDATE)


}
