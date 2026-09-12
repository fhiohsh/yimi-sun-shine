package com.sky.controller.user;

import com.sky.controller.request.UserCouponRequest;
import com.sky.controller.response.CouponNewResponse;
import com.sky.dto.OrderDTO;
import com.sky.entity.CouponTemplateReal;
import com.sky.entity.CouponUserReal;
import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.newcoupon.CouponRealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/25 0:18
 */
@RestController
@RequestMapping("/user/coupon")
@Slf4j
@Api(tags = "优惠券相关接口")
public class CouponRealController {

    @Autowired
    private CouponRealService couponRealService;

    @PostMapping("/userClaim")
    @ApiOperation("用户领取优惠券")
    public Result<String> userClaimCoupon(@RequestBody UserCouponRequest userCouponRequest){
        couponRealService.userClaimCoupon(userCouponRequest.getCouponIds());
        return Result.success();
    }

    @GetMapping("/activity")
    @ApiOperation("获取所有优惠券")
    public Result<List<CouponNewResponse>> getActivityCoupon(){
        return Result.success(couponRealService.getActCoupons());
    }

    @GetMapping("/list")
    @ApiOperation("用户查看个人优惠券")
    public Result<List<CouponNewResponse>> getUserCoupons(){
        return Result.success(couponRealService.getUserCoupons());
    }

    @PostMapping("/redeem")
    @ApiOperation("用户核销优惠券")
    public Result<String> redeemCoupon(@RequestParam Integer couponId,
                                       @RequestParam Integer orderId){
        couponRealService.redeemCoupon(couponId, orderId);
        return Result.success();
    }

}
