package com.sky.mapper;

import com.sky.entity.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GoodsCategoryMapper {

    @Select("select * from goods_category")
    GoodsCategory queryCategory();
}
