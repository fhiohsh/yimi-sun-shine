package com.sky.dto;

import com.sky.entity.GoodsAttributes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Joey
 * @Description: 商品添加、修改等
 * @date:2024/5/28 18:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDTO implements Serializable {
    private Long id;
    private Long typeId;
    private String name;
    private BigDecimal price;
    private Integer num;
    private String image;
    private String description;
    private String details;
    private List<GoodsAttributes> attrList = new ArrayList<>();;
    private Integer status;
    private String attrInfo;

    public BigDecimal getTotalPrice() {
        if (price == null || num == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(num));
    }
}
