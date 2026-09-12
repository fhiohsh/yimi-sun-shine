package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Joey
 * @Description: 商品分类实体
 * @date:2024/5/22 19:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long type;
    private String name;
}
