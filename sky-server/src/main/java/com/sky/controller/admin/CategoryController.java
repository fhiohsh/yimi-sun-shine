package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.vo.TypeAllVO;
import com.sky.vo.TypeInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Joey
 * @Description: 分类接口
 * @date:2024/5/29 20:49
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;
    /**
     * 分类列表
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("父级含子级分类")
    public Result<List> typeList(){
        List<TypeInfoVO> voList = categoryService.typeList();
        return Result.success(voList);
    }

    @GetMapping("/allList")
    @ApiOperation("所有分类列表")
    public Result<TypeAllVO> typeAllList(){
        TypeAllVO typeAllVO = categoryService.typeAllList();
        return Result.success(typeAllVO);
    }


    @GetMapping("/style")
    @ApiOperation("id查询子分类")
    public Result<TypeAllVO> typeStyleById(@Param("id") Long id){
        TypeAllVO typeAllVO = categoryService.queryByCategoryId(id);
        return Result.success(typeAllVO);
    }



}
