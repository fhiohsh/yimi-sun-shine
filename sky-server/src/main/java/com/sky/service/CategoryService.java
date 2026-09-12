package com.sky.service;

import com.sky.vo.TypeAllVO;
import com.sky.vo.TypeInfoVO;

import java.util.List;

public interface CategoryService {

    /**
     * 所有分类
     * @return
     */
    List<TypeInfoVO> typeList();

    /**
     * 分类列表 categoryList + categoryStyleList
     * @return
     */
    TypeAllVO typeAllList();

    /**
     * ID查询子分类
     * @param id
     * @return
     */
    TypeAllVO queryByCategoryId(Long id);
}
