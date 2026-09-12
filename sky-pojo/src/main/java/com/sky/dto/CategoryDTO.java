package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Joey
 * @Description:
 * @date:2024/5/29 20:51
 */
@Data
public class CategoryDTO implements Serializable {
    //主键
    private Long id;

    //类型 1 菜品分类 2 套餐分类
    private Integer typeId;

    private Integer categoryDetailId;

    //分类名称
    private String name;

    //排序
    private Integer sort;
}
