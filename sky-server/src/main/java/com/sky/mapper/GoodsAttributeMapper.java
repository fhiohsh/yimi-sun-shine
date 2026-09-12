package com.sky.mapper;

import com.sky.entity.Attributes;
import com.sky.entity.GoodsAttributes;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsAttributeMapper {

    //批量插入
    void insertAttrBatch(List<GoodsAttributes> goodsAttributesList);

    //goods_id查询
    @Select("select * from goods_attributes where goods_id=#{goodsId}")
    List<GoodsAttributes> queryGoodsAttr(Long goodsId);

    //查询统一规格
    @Select("select * from attributes")
    List<Attributes> query();

    @Delete("delete from goods_attributes where goods_id = #{goodsId}")
    void deleteAttr(Long goodsId);
}
