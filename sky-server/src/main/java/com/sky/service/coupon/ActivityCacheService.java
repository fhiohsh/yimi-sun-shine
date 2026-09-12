package com.sky.service.coupon;

import com.sky.entity.CouponActivity;

public interface ActivityCacheService {
    /**
     * 设置优惠券活动缓存
     * @param couponActivity
     */
    void setCouponActivityCache(CouponActivity couponActivity);

    /**
     * 获取优惠券活动的缓存
     * @param couponActivityId
     * @return
     */
    CouponActivity getCouponActivityCache(Long couponActivityId);

    /**
     * 删除缓存
     * @param couponActivityId
     */
    void invalidateCouponActivityCache(Long couponActivityId);
}
