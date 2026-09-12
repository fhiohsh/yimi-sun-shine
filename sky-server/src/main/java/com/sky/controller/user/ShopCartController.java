package com.sky.controller.user;

import com.sky.entity.ShopCart;
import com.sky.result.Result;
import com.sky.service.ShopCartService;
import com.sky.vo.ShopCartVO;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/21 16:35
 */

@RestController
@RequestMapping("/user/cart")
public class ShopCartController {
    @Autowired
    private ShopCartService shopCartService;

    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result<?> addToCart(@RequestBody ShopCart shopCart) {
        return Result.success(shopCartService.addToCart(shopCart));
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除购物车商品")
    public Result<?> deleteFromCart(@PathVariable Integer id) {
        return Result.success(shopCartService.deleteFromCart(id));
    }

    @PostMapping("/update/{id}/{quantity}")
    @ApiOperation("修改商品数量")
    public Result<?> updateQuantity(@PathVariable Integer id, @PathVariable Integer quantity) {
        return Result.success(shopCartService.updateQuantity(id, quantity));
    }

    @GetMapping("/list")
    @ApiOperation("获取用户购物车列表")
    public Result<List<ShopCartVO>> getCartList() {
        return Result.success(shopCartService.getCartListWithProductInfo());
    }
}
