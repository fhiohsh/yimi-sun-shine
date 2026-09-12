package com.sky.controller.user;

import com.sky.controller.response.OrderResponse;
import com.sky.dto.OrderAddressDTO;
import com.sky.dto.OrderDTO;
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
 * @Description: order
 * @date:2024/9/17 1:19
 */

@RestController
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    @ApiOperation("获取用户订单列表")
    public Result<List<OrderResponse>> getOrderByUser(@RequestParam(value = "order_status",required = false) Integer orderStatus){
        return Result.success(orderService.getOrder(orderStatus));
    }

    @GetMapping("/orderDetail/{orderId}")
    @ApiOperation("获取用户订单详情")
    public Result<OrderDetailVO> getOrderDetailByUser(@PathVariable Integer orderId){
        OrderDetailVO orderDetail = orderService.getOrderDetail(orderId);
        return Result.success(orderDetail);
    }

    //生成订单
    @PostMapping("/addOrder")
    @ApiOperation("生成订单")
    public Result<Orders> addOrder(@RequestBody OrderDTO orderDTO){
        return Result.success(orderService.addOrder(orderDTO));
    }


    //支付订单
    @PostMapping("/payOrder")
    @ApiOperation("支付订单")
    public Result<Orders> payOrder(@RequestBody OrderDTO orderDTO){
        return Result.success(orderService.payOrder(orderDTO));
    }


    //修改订单状态
    @PostMapping("/update")
    @ApiOperation("更新订单状态")
    public Result<String> updateOrder( @RequestBody OrderAddressDTO orderAddressDTO){
        orderService.updateOrderStatus(orderAddressDTO);
        return Result.success("订单"+orderAddressDTO.getOrderId()+",状态更新");
    }


    //删除订单记录
}
