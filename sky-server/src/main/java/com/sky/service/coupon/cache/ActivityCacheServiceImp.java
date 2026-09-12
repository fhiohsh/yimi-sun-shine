package com.sky.service.coupon.cache;

import cn.hutool.json.JSONUtil;
import com.sky.constant.CouponActivityKeyConstant;
import com.sky.entity.CouponActivity;
import com.sky.service.coupon.ActivityCacheService;
import com.sky.utils.RedisUtil;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sky.constant.CouponCacheKeyConstant.getCouponActivityKey;

/**
 * @author: Joey
 * @Description:
 * @date:2024/7/18 16:42
 */
@Service
public class ActivityCacheServiceImp implements ActivityCacheService {

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public void setCouponActivityCache(CouponActivity couponActivity) {
        Map<String, Object> couponActivityCacheMap = new HashMap<>();
        couponActivityCacheMap.put(CouponActivityKeyConstant.ACTIVITY_INFO, JSONUtil.toJsonStr(couponActivity));
        //todo 取消实体里存储数量
//        couponActivityCacheMap.put(CouponActivityKeyConstant.TOTAL_NUMBER, String.valueOf(couponActivity.getTotalNumber()));
        redisUtil.set("mall_coupon_stock:"+couponActivity.getId().toString(),couponActivity.getTotalNumber());
        redisUtil.hmset(getCouponActivityKey(couponActivity.getId()), couponActivityCacheMap);
    }



    @Override
    public CouponActivity getCouponActivityCache(Long couponActivityId) {
        //redis获取
        Map<String, Object> couponActivityCacheMap = redisUtil.hmget(getCouponActivityKey(couponActivityId));
        if (Objects.isNull(couponActivityCacheMap) || StringUtil.isBlank((String) couponActivityCacheMap.get(CouponActivityKeyConstant.ACTIVITY_INFO))) {
            return null;
        }                                   //todo JSONUtil.toBean() 将JSON格式的数据转换为Java对象
        CouponActivity couponActivityCache =
                JSONUtil.toBean((String) couponActivityCacheMap
                                .get(CouponActivityKeyConstant.ACTIVITY_INFO), CouponActivity.class);
//        Long totalNumber = Long.valueOf(String.valueOf(couponActivityCacheMap.get(CouponActivityKeyConstant.TOTAL_NUMBER)));
        Integer totalNUm = (Integer) redisUtil.get("mall_coupon_stock:" + couponActivityId.toString());
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setId(couponActivityCache.getId());
        couponActivity.setName(couponActivityCache.getName());
        couponActivity.setCouponTemplateCode(couponActivityCache.getCouponTemplateCode());
        couponActivity.setTotalNumber(totalNUm.longValue());
        couponActivity.setLimitNumber(couponActivityCache.getLimitNumber());
        couponActivity.setStatus(couponActivityCache.getStatus());
        couponActivity.setBeginTime(couponActivityCache.getBeginTime());
        couponActivity.setEndTime(couponActivityCache.getEndTime());
        couponActivity.setCreateTime(couponActivityCache.getCreateTime());
        couponActivity.setUpdateTime(couponActivityCache.getUpdateTime());
        return couponActivity;
    }

    @Override
    public void invalidateCouponActivityCache(Long couponActivityId) {
        redisUtil.del(getCouponActivityKey(couponActivityId));
    }
}
