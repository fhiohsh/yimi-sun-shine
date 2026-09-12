package com.sky.controller.admin;

import com.sky.controller.request.AddCouponActivityRequest;
import com.sky.controller.request.CouponTemplateRequest;
import com.sky.controller.request.UserCouponRequest;
import com.sky.controller.request.VerificationCouponRequest;
import com.sky.request.ReceiveCouponRequest;
import com.sky.result.Result;
import com.sky.service.coupon.CouponService;
import com.sky.service.coupon.CouponTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author: Joey
 * @Description: 測試用
 * @date:2024/7/18 14:48
 */
@RestController
@RequestMapping("/admin/coupon")
@Api(tags = "优惠券接口")
@Slf4j
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponTemplateService couponTemplateService;

    @PostMapping("/addCouponTemplate")
    @ApiOperation("添加-优惠券模版信息")
    public Result addCouponTemplate(@RequestBody @Valid CouponTemplateRequest request) {
        return Result.success(couponTemplateService.addCouponTemplate(request));
    }

    @GetMapping("/getCouponTemplate")
    @ApiOperation("查询-优惠券模版信息")
    @Parameter(name = "couponTemplateCode", description = "券模版Code", required = true)
    public Result getCouponTemplate(@NotBlank(message = "券模版Code不能为空") @RequestParam("couponTemplateCode") String couponTemplateCode) {
        return Result.success(couponService.getCouponTemplate(couponTemplateCode));
    }

    @PostMapping("/addCouponActivity")
    @ApiOperation("新建优惠券活动")
    public Result addCouponActivity(@RequestBody @Valid AddCouponActivityRequest request) {
        return Result.success(couponService.addCouponActivity(request));
    }

    @PostMapping("/getCouponCenterList")
    @ApiOperation("查询优惠券活动列表")
    public Result getCouponCenterList() {
        return Result.success(couponService.getCouponCenterList());
    }


    @SneakyThrows
    @PostMapping("/receive")
    @ApiOperation("领取优惠券")
    public Result receive(@RequestBody @Valid ReceiveCouponRequest request) {
        return Result.success(couponService.receive(request));
    }

    @PostMapping("/getList")
    @ApiOperation("查询用户优惠券列表")
    public Result getList(@RequestBody @Valid UserCouponRequest request) {
        return Result.success(couponService.getList(request));
    }

    @GetMapping("/getCoupon")
    @ApiOperation("查询个人优惠券信息")
    public Result getCoupon(@RequestParam("code") @NotBlank String code) {
        return Result.success(couponService.getCoupon(code));
    }

    @SneakyThrows
    @PostMapping("/verification")
    @ApiOperation("核销优惠券")
    public Result verification(@RequestBody @Valid VerificationCouponRequest request) {
        return Result.success(couponService.verification(request));
    }
}
