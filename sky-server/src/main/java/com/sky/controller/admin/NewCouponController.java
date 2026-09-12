package com.sky.controller.admin;

import com.sky.controller.request.CouponTemplateStatusRequest;
import com.sky.entity.CouponActivityReal;
import com.sky.entity.CouponTemplateReal;
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
 * @date:2024/9/25 0:48
 */
@RestController
@RequestMapping("/admin/new/coupon")
@Api(tags = "优惠券新接口")
@Slf4j
public class NewCouponController {
    @Autowired
    private CouponRealService couponRealService;

    @PostMapping("/create/template")
    @ApiOperation("创建优惠券模板")
    public Result<String> createCouponTemplate(@RequestBody CouponTemplateReal couponTemplateReal){
        couponRealService.createCoupon(couponTemplateReal);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("获取优惠券模板")
    public Result<List<CouponTemplateReal>> getCouponTemplates(@RequestParam(value = "type",required = false) Integer type){
        return Result.success(couponRealService.getCouponTemplates(type));
    }

    @PostMapping("/active")
    @ApiOperation("启用停用优惠券模板")
    public Result<String> updateCouponTemplate(@RequestBody CouponTemplateStatusRequest couponTemplateStatusRequest){
        boolean templateStatus = couponRealService.
                updateCouponTemplateStatus(couponTemplateStatusRequest.getTemplateId(), couponTemplateStatusRequest.getIsActive());
        if (templateStatus) {
            return Result.success();
        }
        return Result.error("更新失败 模板ID: " + couponTemplateStatusRequest.getTemplateId());
    }

    @PostMapping("/create/activity")
    @ApiOperation("创建优惠券活动")
    public Result<String> createCouponActivity(@RequestBody CouponActivityReal couponActivityReal){
        couponRealService.createCouponActivity(couponActivityReal);
        return Result.success();
    }

    @GetMapping("/list/activity")
    @ApiOperation("获取优惠券活动列表")
    public Result<List<CouponActivityReal>> getCouponActivity(){
        return Result.success(couponRealService.getCouponActivity());
    }

    //updateCouponActivityStatus 活动开启
    @PostMapping("/activity/open")
    @ApiOperation("启用停用优惠券活动")
    public Result<String> updateCouponActivity(@RequestBody CouponTemplateStatusRequest couponTemplateStatusRequest){
        boolean templateStatus = couponRealService.
                updateCouponActivityStatus(couponTemplateStatusRequest.getActId(), couponTemplateStatusRequest.getActivityStatus());
        if (templateStatus) {
            return Result.success();
        }
        return Result.error("更新失败 模板ID: " + couponTemplateStatusRequest.getTemplateId());
    }
    //获取所有活动列表


    //菜单： 模板列表、 活动列表、


}
