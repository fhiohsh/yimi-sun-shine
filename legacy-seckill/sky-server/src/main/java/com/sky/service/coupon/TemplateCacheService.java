package com.sky.service.coupon;

import com.sky.entity.CouponTemplate;

public interface TemplateCacheService {

    /**
     * 放入guava缓存
     * @param couponTemplateCode
     * @param couponTemplate
     */
    void setCouponTemplateCache(String couponTemplateCode, CouponTemplate couponTemplate);

    /**
     * 获取guava缓存
     * @param couponTemplateCode
     * @return
     */
    CouponTemplate getCouponTemplateCache(String couponTemplateCode);

    void invalidateCouponTemplateCache(String couponTemplateCode);
}
