package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.vo.GoodsVo;
import com.sky.vo.TypeInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/6/7 17:00
 */
@RestController()
@RequestMapping("/user/category")
@Slf4j
@Api(tags = "商品分类-app相关接口")
public class CategoryUserController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @ApiOperation("父级含子级分类")
    public Result<List> typeListApp(){
        List<TypeInfoVO> voList = categoryService.typeList();
        return Result.success(voList);
    }
}
