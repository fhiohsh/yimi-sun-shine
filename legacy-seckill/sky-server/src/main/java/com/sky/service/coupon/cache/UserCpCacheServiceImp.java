package com.sky.service.coupon.cache;

import cn.hutool.json.JSONUtil;
import com.sky.entity.Coupon;
import com.sky.service.coupon.UserCpCacheService;
import com.sky.util.CouponRedisLuaUtil;
import com.sky.utils.RedisUtil;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sky.constant.CouponCacheKeyConstant.getCouponKey;
import static com.sky.constant.CouponCacheKeyConstant.getUserCouponKey;
import static com.sky.utils.CouponUtil.calcCouponExpireTime;

/**
 * @author: Joey
 * @Description:
 * @date:2024/7/18 17:00
 */
@Service
public class UserCpCacheServiceImp implements UserCpCacheService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CouponRedisLuaUtil couponRedisLuaUtil;
    @Override
    public boolean setCouponCache(Coupon coupon) {
        return redisUtil.set(getCouponKey(coupon.getCode()), JSONUtil.toJsonStr(coupon), calcCouponExpireTime(coupon.getBeginTime(), coupon.getEndTime()));
    }



    //缓存时间
    @Override
    public boolean setCouponCache(Coupon coupon, Long time, TimeUnit timeUnit) {
        return redisUtil.set(getCouponKey(coupon.getCode()), JSONUtil.toJsonStr(coupon),time,timeUnit);
    }



    @Override
    public Coupon getCouponCache(String code) {
        // todo
        String result = (String) redisUtil.get(getCouponKey(code));
        return StringUtil.isNotEmpty(result) ? JSONUtil.toBean(result, Coupon.class) : null;
    }

    @Override
    public List<Coupon> batchGetCouponCache(List<String> codes) {
        return codes.stream()
                .map(code -> JSONUtil.toBean((String) redisUtil.get(getCouponKey(code)), Coupon.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean batchSetCouponCache(List<Coupon> coupons) {
        return couponRedisLuaUtil.batchSet(coupons);
    }

    @Override
    public List<String> getUserCouponCodeList(Long mobile) {
        return redisUtil.lGet(
                getUserCouponKey(mobile),
                0,
                500
        );
    }

    @Override
    public boolean addUserCouponCode(Long mobile, String couponCode) {
        return redisUtil.lSet(getUserCouponKey(mobile), couponCode);
    }

    //核销优惠券信息1 删除
    @Override
    public boolean delUserCouponCode(Coupon coupon) {
        return redisUtil.lRemove(getUserCouponKey(coupon.getMobile()), 0L, coupon.getCode()) > 0;
    }

    //核销优惠券信息2
    @Override
    public boolean delCouponCache(Coupon coupon) {
        return redisUtil.del(getCouponKey(coupon.getCode())) > 0;
    }
}
