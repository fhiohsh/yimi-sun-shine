package com.sky.mapper;

import com.sky.entity.GoodsCategory;
import com.sky.entity.GoodsCategoryStyle;
import com.sky.vo.TypeInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {


    @Select("select * from goods_category")
    List<GoodsCategory> queryType();

    @Select("select * from goods_category_style")
    List<GoodsCategoryStyle> queryChildType();

    @Select("select c.id from goods_category c left join goods_category_style s on s.type_id = c.id " +
            "where s.id=#{sid} limit 1")
    GoodsCategory queryId(Long sid);

    @Select("select * from goods_category_style where type_id = #{typeId}")
    List<GoodsCategoryStyle> queryChildTypeId(Long typeId);

}
