package com.sky.service.impl;

import com.sky.dto.TypeInfoDTO;
import com.sky.entity.GoodsCategory;
import com.sky.entity.GoodsCategoryStyle;
import com.sky.mapper.CategoryMapper;
import com.sky.service.CategoryService;
import com.sky.vo.TypeAllVO;
import com.sky.vo.TypeInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/5/29 20:59
 */
@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<TypeInfoVO> typeList() {
        List<GoodsCategoryStyle> CategoryStyles = categoryMapper.queryChildType();
        List<GoodsCategory> Categories = categoryMapper.queryType();

        TypeInfoVO typeInfoVO=null;
        TypeInfoDTO infoDTO=null;
        List<TypeInfoVO> voList= new ArrayList<>();
        List<TypeInfoDTO> infoDTOList = null;
        for (GoodsCategory c:Categories) {
            typeInfoVO = new TypeInfoVO();
            typeInfoVO.setLabel(c.getName());
            typeInfoVO.setValue(c.getId());

            infoDTOList = new ArrayList<>();
            //  c.getId == 1
            for (GoodsCategoryStyle s : CategoryStyles) {
                infoDTO = new TypeInfoDTO();
                if(c.getId() == (s.getTypeId())){//查询父级id下分类
                    infoDTO.setLabel(s.getStyleName());
                    infoDTO.setValue(s.getId());
                    infoDTO.setImage(s.getImage());
                    infoDTOList.add(infoDTO);
                }
            }
            typeInfoVO.setChildren(infoDTOList);
//            typeInfoVO.setHasChildren(true);
            voList.add(typeInfoVO);
        }
        return voList;
    }

    @Override
    public TypeAllVO typeAllList() {
        List<GoodsCategoryStyle> CategoryStyles = categoryMapper.queryChildType();
        List<GoodsCategory> categories = categoryMapper.queryType();
        TypeAllVO typeAllVO = new TypeAllVO();
        typeAllVO.setCategoryList(categories);
        typeAllVO.setCategoryStyleList(CategoryStyles);
        return typeAllVO;
    }

    @Override
    public TypeAllVO queryByCategoryId(Long id) {
        List<GoodsCategoryStyle> categoryStyles = categoryMapper.queryChildTypeId(id);
        TypeAllVO typeStyleVO = new TypeAllVO();
        typeStyleVO.setCategoryStyleList(categoryStyles);
        return typeStyleVO;
    }
}
