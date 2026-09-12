package com.sky.service.impl;

import com.sky.entity.Attributes;
import com.sky.entity.GoodsAttributes;
import com.sky.mapper.GoodsAttributeMapper;
import com.sky.service.GoodsAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Joey
 * @Description: 规格业务处理
 * @date:2024/5/30 14:17
 */
@Service
public class GoodsAttributeServiceImp implements GoodsAttributeService {

    @Autowired
    private GoodsAttributeMapper goodsAttributeMapper;
    @Override
    public List<Attributes> query() {
        return goodsAttributeMapper.query();
    }
}
