package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.PageGoodsQueryDTO;
import com.sky.entity.Goods;
import com.sky.enumeration.OperationType;
import com.sky.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 插入商品
     * @param goods
     */
    @AutoFill(OperationType.INSERT)
    void insertGoods(Goods goods);

    /**
     * 插入商品
     * @param goods
     */
    @AutoFill(OperationType.UPDATE)
    void updateGoods(Goods goods);

    /**
     * 分页查询
     * @param pageGoodsQueryDTO
     * @return
     */
    Page<GoodsVo> pageGoodsQuery(PageGoodsQueryDTO pageGoodsQueryDTO);

    List<GoodsVo> goodsListQuery(PageGoodsQueryDTO pageGoodsQueryDTO);

    List<GoodsVo> goodsListRandom(PageGoodsQueryDTO pageGoodsQueryDTO);



    /**
     * 根据id查询
     * @param goodsId
     * @return
     */
    @Select("select * from goods where id=#{goodsId}")
    Goods queryGoods(Long goodsId);

}
