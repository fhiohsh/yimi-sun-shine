package com.sky.vo;

import com.sky.entity.GoodsCategory;
import com.sky.entity.GoodsCategoryStyle;
import lombok.Data;

import java.util.List;

/**
 * @author: Joey
 * @Description: 所有分类 大+小
 * @date:2024/5/30 14:56
 */
@Data
public class TypeAllVO {
    //     name: '鞋类',
    //     id: 1

    //           name: '外套',
    //           id: 4
    List<GoodsCategory> categoryList;
    List<GoodsCategoryStyle> categoryStyleList;
}
