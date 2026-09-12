package com.sky.mapper;

import com.sky.entity.GoodsCategoryStyle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 具体分类
 */
@Mapper
public interface GoodsCategoryStyleMapper {

    @Select("select * from goods_category_style")
    GoodsCategoryStyle queryCategoryStyle();
}
