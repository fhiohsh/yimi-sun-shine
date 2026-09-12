package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Joey
 * @Description: 商品规格实体
 * @date:2024/5/22 19:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsAttributes implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long goodsId;
    private String name;//name
    private String param;//list
}
