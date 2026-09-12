package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.GoodsDTO;
import com.sky.dto.PageGoodsQueryDTO;
import com.sky.entity.Goods;
import com.sky.entity.GoodsAttributes;
import com.sky.entity.GoodsCategory;
import com.sky.entity.Orders;
import com.sky.exception.SourceNotFoundException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.GoodsAttributeMapper;
import com.sky.mapper.GoodsMapper;
import com.sky.result.PageResult;
import com.sky.service.GoodsService;
import com.sky.vo.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/5/28 21:21
 */
@Service
public class GoodsServiceImp implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsAttributeMapper goodsAttributeMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional
    public void insertGoods(GoodsDTO goodsDTO) {
        //添加商品
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsDTO,goods);
        goodsMapper.insertGoods(goods);
        Long goodsId = goods.getId();

        //添加商品参数
        List<GoodsAttributes> AttrList = goodsDTO.getAttrList();

        if(AttrList!=null && AttrList.size()>0){
            AttrList.forEach(attr -> {
                attr.setGoodsId(goodsId);
            });
        }
        goodsAttributeMapper.insertAttrBatch(AttrList);
    }

    @Override
    public PageResult goodsListPage(PageGoodsQueryDTO pageGoodsQueryDTO) {
        PageHelper.startPage(pageGoodsQueryDTO.getPage(),pageGoodsQueryDTO.getPageSize());
        Page<GoodsVo> goodsVos = goodsMapper.pageGoodsQuery(pageGoodsQueryDTO);
        long total = goodsVos.getTotal();
        List<GoodsVo> records = goodsVos.getResult();
        return new PageResult(total,records);
    }

    public List<GoodsVo> goodsListUser(PageGoodsQueryDTO pageGoodsQueryDTO){
        if(pageGoodsQueryDTO.getCategoryId() == 0){
            pageGoodsQueryDTO.setCategoryId(null);
        }
        return goodsMapper.goodsListQuery(pageGoodsQueryDTO);
    }

    @Override
    public List<GoodsVo> goodsListRandom(PageGoodsQueryDTO pageGoodsQueryDTO) {
        return goodsMapper.goodsListRandom(pageGoodsQueryDTO);
    }

    @Override
    public GoodsVo queryGoodsById(Long goodsId) {
        if(goodsId == null){
            throw new SourceNotFoundException("请求资源未找到");
        }
        Goods goods = goodsMapper.queryGoods(goodsId);
        GoodsVo goodsVo = new GoodsVo();
        BeanUtils.copyProperties(goods,goodsVo);
        List<GoodsAttributes> attributes = goodsAttributeMapper.queryGoodsAttr(goodsId);
        goodsVo.setShortDesc(goods.getDescription());
        goodsVo.setDescription(goods.getDetails());
        goodsVo.setAttrList(attributes);
        goodsVo.setCategoryDetailId(goods.getTypeId());
        GoodsCategory category = categoryMapper.queryId(goods.getTypeId());
        goodsVo.setCategoryId(category.getId());
        return goodsVo;
    }

    @Transactional
    public void updateGoods(GoodsDTO goodsDTO) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsDTO,goods);
        //goods
        if(goods!=null){
            goodsMapper.updateGoods(goods);
        }
        //批量更新
        goodsAttributeMapper.deleteAttr(goodsDTO.getId());
        List<GoodsAttributes> AttrList = goodsDTO.getAttrList();
        if(AttrList!=null && AttrList.size()>0){
            AttrList.forEach(attr -> {
                attr.setGoodsId(goodsDTO.getId());
            });
        }
        goodsAttributeMapper.insertAttrBatch(AttrList);
    }
}
