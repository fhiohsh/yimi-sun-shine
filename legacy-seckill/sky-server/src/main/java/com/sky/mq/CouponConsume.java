package com.sky.mq;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.entity.Coupon;
import com.sky.entity.CouponActivity;
import com.sky.entity.CouponActivityLog;
import com.sky.enumeration.IdempotentStatusEnum;
import com.sky.exception.BaseException;
import com.sky.mapper.CouponActivityLogMapper;
import com.sky.mapper.CouponActivityMapper;
import com.sky.mapper.CouponMapper;
import com.sky.request.ReceiveCouponRequest;
import com.sky.service.Idem.IdempotentService;
import com.sky.utils.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.sky.service.coupon.CouponServiceImp.getCouponActivityLog;

/**
 * @author: Joey
 * @Description: 优惠券监听mq
 * @date:2024/7/19 16:22
 */
@Component
@Slf4j
public class CouponConsume {

    @Autowired
    private CouponActivityMapper couponActivityMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponActivityLogMapper couponActivityLogMapper;

    @Autowired
    private IdempotentService idempotentService;

    /**
     * 领取优惠券-MQ监听
     * @param couponMap
     * @param mp
     */
    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "coupon.queue", durable = "true"),
            exchange = @Exchange(name = "coupon.topic"), key = "coupon.joey"))
    public void listenCouponSuccess(Map couponMap, MessageProperties mp){
        log.info("MQ接收到数据=> {},消息ID = {}",couponMap,mp.getMessageId());
        // 幂等判断消息是否重复消费
        if (Boolean.FALSE.equals(idempotentService.getIdempotentLock(String.valueOf(mp.getMessageId())))) {
            // 命中幂等，查询当前处理进度
            String idempotentResult = idempotentService.getIdempotentResult(mp.getMessageId());
            if (StringUtils.isNotBlank(idempotentResult)) {
                // 如果处理中，则丢回消息队列重试
                if (IdempotentStatusEnum.isInProgress(idempotentResult)) {
                    throw new BaseException("receiveRegisterUserCoupon MQ error");
                }
                // 如果处理完成直接返回处理结果，
                if (IdempotentStatusEnum.isFinish(idempotentResult)) {
                    return;
                }
            }
            throw new BaseException("receiveRegisterUserCoupon MQ error");
        }

        Integer stock = (Integer) couponMap.get("stock");
        LinkedHashMap infomap= (LinkedHashMap) couponMap.get("couponInfo");
        ReceiveCouponRequest request = JSON.parseObject(JSON.toJSONString(infomap), ReceiveCouponRequest.class);
        LinkedHashMap coupons = (LinkedHashMap) couponMap.get("coupon");
        Coupon coupon = JSON.parseObject(JSON.toJSONString(coupons), Coupon.class);

        try {
            QueryWrapper<CouponActivity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", request.getCouponActivityID());
            CouponActivity activity = couponActivityMapper.selectOne(queryWrapper);
            activity.setTotalNumber(stock.longValue());
            couponActivityMapper.updateById(activity);
            //用户优惠券数据库更新
            couponMapper.insert(coupon);
            // 保存领券活动参与记录Log
            CouponActivityLog couponActivityLog = getCouponActivityLog(request, coupon);
            couponActivityLogMapper.insert(couponActivityLog);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // 处理完成后，设置处理状态为 已完成
        idempotentService.setFinish(mp.getMessageId());
        log.info("消费消息=> 用户 = {},优惠券code = {}",coupon.getUserId(),coupon.getCode());
        //todo 后期socket通知
    }


}
