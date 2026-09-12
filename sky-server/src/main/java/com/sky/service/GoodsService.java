package com.sky.service;

import com.sky.dto.GoodsDTO;
import com.sky.dto.PageGoodsQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.GoodsVo;

import java.util.List;

public interface GoodsService {

    /**
     * 新增
     * @param goodsDTO
     */
    void insertGoods(GoodsDTO goodsDTO);

    /**
     * 修改
     * @param goodsDTO
     */
    void updateGoods(GoodsDTO goodsDTO);


    /**
     * 分页查询
     */
    PageResult goodsListPage(PageGoodsQueryDTO pageGoodsQueryDTO);

    List<GoodsVo> goodsListUser(PageGoodsQueryDTO pageGoodsQueryDTO);

    /**
     * 随机查询 哈哈哈哈
     * @param pageGoodsQueryDTO
     * @return
     */
    List<GoodsVo> goodsListRandom(PageGoodsQueryDTO pageGoodsQueryDTO);

    /**
     * 根据id查询goods
     * @param goodsId
     * @return
     */
    GoodsVo queryGoodsById(Long goodsId);


}
