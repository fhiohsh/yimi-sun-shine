package com.sky.service.coupon;

import com.sky.controller.request.CouponTemplateRequest;

public interface CouponTemplateService {
    /**
     * 添加优惠券模板
     * @param request
     * @return
     */
    boolean addCouponTemplate(CouponTemplateRequest request);
}
