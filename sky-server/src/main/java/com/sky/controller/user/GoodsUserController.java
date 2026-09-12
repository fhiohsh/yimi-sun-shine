package com.sky.controller.user;

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

import java.util.List;

/**
 * @author: Joey
 * @Description: app商品相关接口
 * @date:2024/6/1 14:58
 */
@RestController("GoodsUserController")
@RequestMapping("/user/goods")
@Slf4j
@Api(tags = "app商品相关接口")
public class GoodsUserController {

    @Autowired
    private GoodsService goodsService;


    @GetMapping("/page")
    @ApiOperation("商品分页查询")
    public Result<List<GoodsVo>> GoodsListPage(PageGoodsQueryDTO pageGoodsQueryDTO){
        return Result.success(goodsService.goodsListUser(pageGoodsQueryDTO));
    }

    @GetMapping("/rec/list")
    @ApiOperation("商品随机查询")
    public Result<List<GoodsVo>> GoodsListRecommend(PageGoodsQueryDTO pageGoodsQueryDTO){
        return Result.success(goodsService.goodsListRandom(pageGoodsQueryDTO));
    }

    @GetMapping("/query")
    @ApiOperation("商品ID查询")
    public Result<GoodsVo> selectGoodsById(@Param("id") Long id){
        log.info("查询商品,{}",id);
        GoodsVo goods = goodsService.queryGoodsById(id);
        return Result.success(goods);
    }




}
