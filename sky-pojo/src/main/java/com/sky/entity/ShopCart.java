package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/21 16:32
 */
@Data
@Builder
@TableName("shop_cart")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ShopCart {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer Id;
    private Integer userId;
    private Integer productId;//需要接收
    private String attrInfo;//需要接收
    private Integer quantity;//需要接收

}
