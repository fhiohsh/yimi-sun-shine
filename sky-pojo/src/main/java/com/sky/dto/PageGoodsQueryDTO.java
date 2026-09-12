package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Joey
 * @Description:
 * @date:2024/5/28 21:55
 */
@Data
public class PageGoodsQueryDTO implements Serializable {
    private int page;
    private int pageSize;
    //商品名称
    private String name;
    //分类id
    private Integer categoryId;//详细分类id
    //状态 0表示禁用 1表示启用
    private Integer status;
}
