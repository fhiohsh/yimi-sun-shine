package com.sky.service.coupon;

import com.sky.controller.request.AddCouponActivityRequest;
import com.sky.controller.request.CouponTemplateRequest;
import com.sky.controller.request.UserCouponRequest;
import com.sky.controller.request.VerificationCouponRequest;
import com.sky.controller.response.CouponActivityResponse;
import com.sky.controller.response.CouponResponse;
import com.sky.entity.CouponTemplate;
import com.sky.exception.BaseException;
import com.sky.request.ReceiveCouponRequest;

import java.util.List;

public interface CouponService {


    /**
     * 添加优惠券活动
     * @param request
     * @return
     */
    boolean addCouponActivity(AddCouponActivityRequest request);
//
//    /**
//     * 添加优惠券模板
//     * @param request
//     * @return
//     */
//    boolean addCouponTemplate(CouponTemplateRequest request);

    /**
     * 查询优惠券模板
     * @param couponTemplateCode
     * @return
     */
    CouponTemplate getCouponTemplate(String couponTemplateCode);

    /**
     *  获取优惠券活动列表
     * @return
     */
    List<CouponActivityResponse> getCouponCenterList();

    /**
     * 抢券
     * @param request
     * @return
     * @throws BaseException
     */
    CouponResponse receive(ReceiveCouponRequest request) throws BaseException;

    /**
     * 核销券、用券
     * @param request
     * @return
     */
    Boolean verification(VerificationCouponRequest request);

    /**
     * 获取用户优惠券列表
     * @param request
     * @return
     */
    List<CouponResponse> getList(UserCouponRequest request);

    /**
     * 获取个人优惠券信息
     * @param code
     * @return
     */
    CouponResponse getCoupon(String code);
}
