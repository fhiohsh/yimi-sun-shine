package com.sky.util;

import cn.hutool.json.JSONUtil;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.sky.constant.CouponActivityKeyConstant;
import com.sky.entity.Coupon;
import com.sky.request.ReceiveCouponRequest;
import com.sky.utils.RedisUtil;
import org.checkerframework.checker.units.qual.A;
import org.redisson.api.RLock;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.sky.constant.CouponCacheKeyConstant.*;
import static com.sky.constant.RedisKeyConstant.getReceiveCouponKey;
import static com.sky.utils.CouponUtil.calcCouponExpireTime;


/**
 * redis工具类
 */
@Component
public class CouponRedisLuaUtil {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 批量保存优惠券信息缓存，并设置券过期时间
     * todo 这里有两个不同的Key，所以操作了两次，后续需要优化成一次执行
     *
     * @param coupons
     * @return
     */
    public boolean batchSet(List<Coupon> coupons) {
        List<String> couponKeys = new ArrayList<>();
        List<String> userCouponKeys = new ArrayList<>();
        StringBuffer couponScript = new StringBuffer();
        StringBuffer userCouponScript = new StringBuffer();
        for (int i = 1; i <= coupons.size(); i++) {
            Coupon coupon = coupons.get(i - 1);
            // 1.保存用户优惠券信息缓存
            couponScript.append("redis.call('setnx', KEYS[" + i + "], '" + JSONUtil.toJsonStr(coupon) + "')");
            // 2.设置优惠券信息过期时间
            couponScript.append("redis.call('expire', KEYS[" + i + "], '" + calcCouponExpireTime(new Date(), coupon.getEndTime()) + "')");
            couponKeys.add(String.format(COUPON_KEY, coupon.getCode()));
            // 3.保存用户拥有的优惠券列表
            userCouponScript.append("redis.call('lpush', KEYS[" + i + "], '" + coupon.getCode() + "')");
            userCouponKeys.add(String.format(USER_COUPON_KEY, coupon.getMobile()));
        }
        DefaultRedisScript couponRedisScript = new DefaultRedisScript();
        couponRedisScript.setScriptText(couponScript.toString());
        DefaultRedisScript userCouponRedisScript = new DefaultRedisScript();
        userCouponRedisScript.setScriptText(userCouponScript.toString());
        try {
            redisTemplate.execute(couponRedisScript, couponKeys);
            redisTemplate.execute(userCouponRedisScript, userCouponKeys);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean receive(ReceiveCouponRequest request) {
        // 领券分多步操作
        //1.扣减领券活动库存，newTotalNumber=-1 为无限量 不扣库存
        // 如果失败，则回滚扣减领券活动库存
        // 如果成功 返回最新库存数量
        //todo lua脚本序列化问题待解决 暂时用分布式锁扣减库存
        RLock lock = redissonClient.getLock("mall_coupon_stock_nx"+request.getCouponActivityID().toString());
        Long decrement=null;
        if (lock.tryLock()) {
            Integer totalNUm = (Integer) redisUtil.get("mall_coupon_stock:" + request.getCouponActivityID().toString());
//            Long num = (Long) redisTemplate.opsForValue().get("mall_coupon_stock" + request.getCouponActivityID().toString());
            try {
                if(totalNUm>0){
                    decrement = redisTemplate.opsForValue().decrement("mall_coupon_stock:"+request.getCouponActivityID().toString());
                }
            }finally {
                if (lock.isLocked()) {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
        }else{
            System.out.println("获取锁失败..排队...");
        }
        return decrement != null && decrement > -1;
    }



}