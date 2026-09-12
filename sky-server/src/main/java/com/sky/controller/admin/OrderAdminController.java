package com.sky.controller.admin;

import com.sky.dto.OrderAddressDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/24 1:41
 */

@RestController
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单相关接口")
public class OrderAdminController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    @ApiOperation("获取所有订单列表")
    public Result<PageResult> getOrderByUser(@RequestParam(value = "order_status",required = false) Integer orderStatus,
                                             @RequestParam(value = "current",required = false) Integer current,
                                             @RequestParam(value = "size",required = false) Integer size){
        return Result.success(orderService.getAllOrder(orderStatus, current, size));
    }


    //获取订单详情
    @GetMapping("/orderDetail")
    @ApiOperation("获取订单详情")
    public Result<OrderDetailVO> getAdminOrderDetailByUser(@RequestParam Integer orderId){
        if (orderId == null) {
            return Result.error("订单id不能为空");
        }
        return Result.success(orderService.getOrderDetail(orderId));
    }

    //更新订单地址
    @PostMapping("/orderDetail/updateAddress")
    @ApiOperation("更新订单详情")
    public Result<String> updateOrderAddress(@RequestBody OrderAddressDTO orderAddressDTO){
        orderService.updateOrderAddress(orderAddressDTO);
        return Result.success();
    }

    @PostMapping("/updateStatus")
    @ApiOperation("更新订单状态")
    public Result<String> updateOrderStatus(@RequestBody OrderAddressDTO orderAddressDTO){
        orderService.updateOrderStatus(orderAddressDTO);
        return Result.success();

    }
    //订单发货


    //取消订单


    //
}
