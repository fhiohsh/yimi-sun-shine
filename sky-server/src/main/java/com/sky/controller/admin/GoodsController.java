package com.sky.controller.admin;

import com.sky.dto.GoodsDTO;
import com.sky.dto.PageGoodsQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.GoodsService;
import com.sky.vo.GoodsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Joey
 * @Description:
 * @date:2024/5/21 17:17
 */
@RestController("GoodsController")
@RequestMapping("/admin/goods")
@Slf4j
@Api(tags = "商品相关接口")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    /**
     * 添加商品
     * @param goodsDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("新增商品")
    public Result<String> addNewGoods(@RequestBody GoodsDTO goodsDTO){
        goodsService.insertGoods(goodsDTO);
        return Result.success();
    }

    @PostMapping("/page")
    @ApiOperation("商品分页查询")
    public Result<PageResult> GoodsListPage(@RequestBody PageGoodsQueryDTO pageGoodsQueryDTO){
        PageResult result = goodsService.goodsListPage(pageGoodsQueryDTO);
        return Result.success(result);
    }

    @GetMapping("/query")
    @ApiOperation("根据id查询商品")
    public Result<GoodsVo> selectGoodsById(@Param("id") Long id){
        log.info("查询商品,{}",id);
        GoodsVo goods = goodsService.queryGoodsById(id);
        return Result.success(goods);
    }

    @PostMapping("/update")
    @ApiOperation("更新商品")
    public Result<String> updateGoods(@RequestBody GoodsDTO goodsDTO){
        log.info("修改商品信息,{}",goodsDTO);
        goodsService.updateGoods(goodsDTO);
        return Result.success();
    }

}
