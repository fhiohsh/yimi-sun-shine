package com.sky.service.coupon;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.constant.CouponActivityKeyConstant;
import com.sky.controller.request.AddCouponActivityRequest;
import com.sky.controller.request.CouponTemplateRequest;
import com.sky.controller.request.UserCouponRequest;
import com.sky.controller.request.VerificationCouponRequest;
import com.sky.controller.response.CouponActivityResponse;
import com.sky.controller.response.CouponResponse;
import com.sky.entity.Coupon;
import com.sky.entity.CouponActivity;
import com.sky.entity.CouponActivityLog;
import com.sky.entity.CouponTemplate;
import com.sky.enumeration.CouponStatusEnum;
import com.sky.enumeration.Errors;
import com.sky.enumeration.StatusEnum;
import com.sky.exception.BaseException;
import com.sky.mapper.CouponActivityLogMapper;
import com.sky.mapper.CouponActivityMapper;
import com.sky.mapper.CouponMapper;
import com.sky.mapper.CouponTemplateMapper;
import com.sky.request.ReceiveCouponRequest;
import com.sky.service.coupon.cache.CouponTemplateCacheServiceImpl;
import com.sky.util.CouponRedisLuaUtil;
import com.sky.utils.RedisUtil;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sky.constant.CouponCacheKeyConstant.getCouponActivityKey;
import static com.sky.constant.RedisKeyConstant.getReceiveCouponKey;
import static com.sky.controller.response.CouponActivityResponse.buildCouponActivityResponse;
import static com.sky.controller.response.CouponResponse.buildCouponResponse;
import static com.sky.entity.Coupon.buildCoupon;
import static com.sky.enumeration.Errors.*;
import static com.sky.utils.CouponUtil.getCouponTemplateCode;

/**
 * @author: Joey
 * @Description:
 * @date:2024/7/18 14:59
 */
@Service
public class CouponServiceImp implements CouponService {
    private static final Logger log = LoggerFactory.getLogger(CouponServiceImp.class);

    @Autowired
    private ActivityCacheService activityCacheService;
    @Autowired
    private TemplateCacheService templateCacheService;
    @Autowired
    private UserCpCacheService userCpCacheService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private CouponActivityMapper couponActivityMapper;
    @Autowired
    private CouponActivityLogMapper couponActivityLogMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponRedisLuaUtil couponRedisLuaUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public boolean addCouponActivity(AddCouponActivityRequest request) {
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setName(request.getName());
        couponActivity.setCouponTemplateCode(request.getCouponTemplateCode());//券模板
        couponActivity.setTotalNumber(request.getTotalNumber());
        couponActivity.setLimitNumber(request.getLimitNumber());
        couponActivity.setVersion(0);//版本号
        couponActivity.setStatus(request.getStatus());
        couponActivity.setBeginTime(request.getBeginTime());
        couponActivity.setEndTime(request.getEndTime());
        couponActivity.setCreateTime(new Date());
        couponActivity.setUpdateTime(new Date());
        int result = couponActivityMapper.insert(couponActivity);
        if (result > 0) {
            // 保存到Redis缓存中
            activityCacheService.setCouponActivityCache(couponActivity);
        }
        return result > 0;
    }

    @Override
    public CouponTemplate getCouponTemplate(String couponTemplateCode) {
        return templateCacheService.getCouponTemplateCache(couponTemplateCode);
    }

    @Override
    public List<CouponActivityResponse> getCouponCenterList() {
        QueryWrapper<CouponActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", StatusEnum.AVAILABLE.getCode());
        List<CouponActivity> couponActivities = couponActivityMapper.selectList(queryWrapper);
        return couponActivities.stream()
                // todo receivedNumber 取真实领取数量 改为查询Redis
                //        QueryWrapper<CouponActivityLog> queryWrapper = new QueryWrapper<>();
                //        queryWrapper.eq("coupon_activity_id", request.getCouponActivityId());
                //        Integer receivedNumber = couponActivityLogMapper.selectCount(queryWrapper);
                .map(couponActivity -> buildCouponActivityResponse(couponActivity, 0L))
                .collect(Collectors.toList());
    }

    /**
     * 抢券 涉及并发
     * @param request
     * @return
     * @throws BaseException
     */
    @Override
    public CouponResponse receive(ReceiveCouponRequest request) throws BaseException {
        CouponResponse couponResponse = null;
        //redis 获取券活动信息
        CouponActivity couponActivityCache = activityCacheService.getCouponActivityCache(request.getCouponActivityID());
        Integer rnum = (Integer) redisUtil.get("mall_coupon_stock:" + request.getCouponActivityID().toString());
        if(couponActivityCache==null || rnum == 0){
            throw new BaseException(NOT_COUPON_INFO_ERROR.getMsg());
        }
        // 检查是否可参与领券活动
        boolean canJoin = checkIsCanJoinActivity(couponActivityCache,request.getMobile());
        if (!canJoin) {
            throw new BaseException(NOT_JOIN_RECEIVE_COUPON_ERROR.getMsg());
        }
        // 开始参与活动 加锁防重复提交 用户id和活动id
        RLock lock = redissonClient.getLock(getReceiveCouponKey(request.getUserId(), request.getCouponActivityID()));
        if (lock.tryLock()) {
            QueryWrapper<CouponActivity> queryWrapper = new QueryWrapper<>();
            try {
                // 领取优惠券，received 用户是否已领取成功
                boolean received = true;
                if (couponActivityCache.existLimit()) { //false 无限制领取 不扣库存
                    received = couponRedisLuaUtil.receive(request);
                }

                // 领取成功 添加用户优惠券信息， todo优化：原子性
                if (received) {
                    CouponTemplate couponTemplateCache = templateCacheService
                            .getCouponTemplateCache(couponActivityCache.getCouponTemplateCode());
                    Coupon coupon = buildCoupon(request, couponTemplateCache);
                    // 保存用户的优惠券信息，
                    userCpCacheService.setCouponCache(coupon);//用户优惠券缓存
                    // 添加用户拥有的优惠券列表 list列表里  手机号+优惠券Code UCP11813966525204893696
                    userCpCacheService.addUserCouponCode(request.getMobile(), coupon.getCode());

                    // todo 优化：改为MQ异步更新Mysql数据库，提高性能
                    // todo 遇到了新问题，MQ如何保障消息不丢失，如何保障Redis和Mysql之间的数据一致性
                    //获取当前库存数量
                    Integer totalNUm = (Integer) redisUtil.get("mall_coupon_stock:" + request.getCouponActivityID().toString());
                    HashMap<String, Object> couponMap = new HashMap<>();
                    couponMap.put("stock",totalNUm);// Redis 当前库存 stock 放进 MQ：
                    couponMap.put("couponInfo",request);
                    couponMap.put("coupon",coupon);
                    //mq异步更新 + 确认机制
                    rabbitTemplate.convertAndSend("coupon.topic", "coupon.joey", couponMap,MsgConfirm());
                    // todo 发送优惠券过期延时队列，更新优惠券过期状态
                    // 组装返回领取的优惠券信息
                    couponResponse = buildCouponResponse(coupon);
                } else {
                    throw new BaseException(RECEIVE_COUPON_ERROR.getMsg());
                }
            } finally {
                if (lock.isLocked()) {
                    // TODO 重点注意：释放锁前要判断当前是否被锁住了lock.isLocked()，否则之间调用unlock会报异常
                    // 严谨一点，防止当前线程释放掉其他线程的锁
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
        } else {
            // 重试获取锁，幂等、防重校验、告警、日志记录、友好的提示等等
            lock.unlock();
        }
        return couponResponse;//返回用户优惠券信息
    }

    //核销券
    @Override
    public Boolean verification(VerificationCouponRequest request) {
        // 加锁
        RLock lock = redissonClient.getLock(request.getCouponCode());
        if (lock.tryLock()) {
            try {
                //查询用户拥有的优惠券列表
                List<String> userCouponCodeList = userCpCacheService.getUserCouponCodeList(request.getMobile());
                // 风控校验：判断当前用户和优惠券是否是同一人，优惠券是否可用
                if (!userCouponCodeList.contains(request.getCouponCode())) {
                    throw new BaseException(NOT_COUPON_INFO_ERROR.getMsg());
                }                                                     //UCP41814333314535333888
                Coupon couponCache = userCpCacheService.getCouponCache(request.getCouponCode());
                // 检查用户和优惠券是否可用 时间和空值判断
                if (checkIsCanVerification(request, couponCache)) {

                    //code+userId查Coupon对象
                    QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("user_id", couponCache.getUserId());
                    queryWrapper.eq("code", couponCache.getCode());
                    Coupon coupon = couponMapper.selectOne(queryWrapper);
                    coupon.setStatus(CouponStatusEnum.USED.getCode()); //修改
                    coupon.setUseTime(new Date());
                    coupon.setUpdateTime(new Date());
                    // todo 改为MQ异步更新Mysql，增加消息不丢失机制   加一个mysql乐观锁
                    int result = couponMapper.updateById(coupon);//用户优惠券状态修改
                    if (result > 0) {
                        // 核销优惠券信息
                        userCpCacheService.delUserCouponCode(coupon);//根据手机号删除用户优惠券
                        userCpCacheService.delCouponCache(coupon);//删除优惠券信息
                    }
                    return result > 0;
                } else {
                    throw new BaseException(NOT_COUPON_INFO_ERROR.getMsg());
                }
            } finally {
                if (lock.isLocked()) {
                    // 防止当前线程释放掉其他线程的锁
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
        }
        return false;
    }
    private boolean checkIsCanVerification(VerificationCouponRequest request, Coupon couponCache) {
        if (Objects.isNull(couponCache)) {
            throw new BaseException(NOT_COUPON_INFO_ERROR.getMsg());
        }
        // 检查是不是本人核销优惠券、是否在可用时间范围内
        if (couponCache.getMobile().equals(request.getMobile())
                && couponCache.getBeginTime().before(new Date())
                && couponCache.getEndTime().after(new Date())) {
            return true;
        }
        return false;
    }
    private CorrelationData MsgConfirm(){
        CorrelationData cd = new CorrelationData();
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                // 2.1.Future发生异常时的处理逻辑，基本不会触发
                log.error("send message fail", ex);
                throw new BaseException("MQ发送消息异常");
            }
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // 2.2.Future接收到回执的处理逻辑，参数中的result就是回执内容
                if(result.isAck()){ // result.isAck()，boolean类型，true代表ack回执，false 代表 nack回执
                    log.info("MQ发送消息成功，收到 ack!");
                }else{ // result.getReason()，String类型，返回nack时的异常描述
                    log.error("发送消息失败，收到 nack, reason : {}", result.getReason());
                    throw new BaseException("MQ发送消息异常");
                }
            }
        });
        return cd;
    }

    @Override
    public List<CouponResponse> getList(UserCouponRequest request) {
        // 查询用户拥有的优惠券Code列表
        List<String> couponCodeList = userCpCacheService.getUserCouponCodeList(request.getMobile());
        // 根据优惠券Code去查询对应的优惠券信息
        List<Coupon> couponCacheList = userCpCacheService.batchGetCouponCache(couponCodeList);
        // 组装优惠券结果返回
        return couponCacheList.stream().map(CouponResponse::buildCouponResponse).collect(Collectors.toList());
    }

    //个人券查看
    @Override
    public CouponResponse getCoupon(String code) {
        CouponResponse response = null;
        Coupon couponCache = userCpCacheService.getCouponCache(code);
        // todo 重点关注：缓存穿透问题，直接返回不查询数据库
        if (Objects.nonNull(couponCache) && couponCache.getCouponTemplateCode().equals("default")) {
            // 命中了自定义的缓存Key,直接返回
            throw new BaseException(Errors.NOT_COUPON_INFO_ERROR.getMsg());
        }
        if (Objects.isNull(couponCache)) {
            // 加锁，todo 加锁KEY
            RLock lock = redissonClient.getLock(code);
            if (lock.tryLock()) {
                try {
                    couponCache = getCouponDB(code);
                } finally {
                    if (lock.isLocked()) {
                        // 严谨一点，防止当前线程释放掉其他线程的锁
                        if (lock.isHeldByCurrentThread()) {
                            lock.unlock();
                        }
                    }
                }
            }

        }
        if (Objects.nonNull(couponCache)) {
            response = buildCouponResponse(couponCache);
        }
        return response;
    }

    /**
     * 数据库查询
     * @param code
     * @return
     */
    private Coupon getCouponDB(String code) {
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        queryWrapper.eq("status", StatusEnum.AVAILABLE.getCode());
        Coupon coupon = couponMapper.selectOne(queryWrapper);
        // 如果为空，则发生缓存穿透问题
        if (Objects.isNull(coupon)) {
            // 设置默认值default，过期时间1分钟, 缓存雪崩怎么解决=> 过期时间改为随机数就好了
            userCpCacheService.setCouponCache(new Coupon(code,new Date()), 1L, TimeUnit.MINUTES);
            return null;
        } else {
            // 判断优惠券是否在有效期内
            if (checkCouponIsAvailable(coupon)) {
                userCpCacheService.setCouponCache(coupon);
                return coupon;
            } else if (checkCouponIsExpired(coupon)) {
                // 优惠券已过期，进行清理
                userCpCacheService.delUserCouponCode(coupon);
                userCpCacheService.delCouponCache(coupon);
                coupon.setStatus(CouponStatusEnum.EXPIRED.getCode());
                couponMapper.updateById(coupon);
            }
        }
        return null;
    }

    /**
     * 日志记录
     * @param request
     * @param coupon
     * @return
     */
    public static CouponActivityLog getCouponActivityLog(ReceiveCouponRequest request, Coupon coupon) {
        CouponActivityLog couponActivityLog = new CouponActivityLog();
        couponActivityLog.setCouponActivityId(request.getCouponActivityID());
        couponActivityLog.setCode(coupon.getCode());
        couponActivityLog.setUserId(request.getUserId());
        couponActivityLog.setMobile(request.getMobile());
        couponActivityLog.setCreateTime(new Date());
        couponActivityLog.setUpdateTime(new Date());
        return couponActivityLog;
    }

    /**
     * 判断是否可以参加活动
     *
     * @param couponActivity
     * @return
     */
    @SneakyThrows
    private boolean checkIsCanJoinActivity(CouponActivity couponActivity,Long mobile) {
        //时间检查
        boolean available = checkActivityIsAvailable(couponActivity);
        if (available) {
            // 查询当前用户已领取数量 todo 优化改为查询Redis
            QueryWrapper<CouponActivityLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("coupon_activity_id", couponActivity.getId());
            queryWrapper.eq("mobile", mobile);
            //todo 手机号和用户必须绑定 不能出现 重复手机号和不同用户ID
            Integer receivedNumber = couponActivityLogMapper.selectCount(queryWrapper);
            if (couponActivity.getLimitNumber() > receivedNumber) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前活动是否生效
     * 当前是活动状态是生效、当前时间在活动范围内、
     *
     * @param couponActivity
     * @return
     */
    private boolean checkActivityIsAvailable(CouponActivity couponActivity) {
        if (couponActivity.getStatus().equals(StatusEnum.AVAILABLE.getCode())
                && couponActivity.getBeginTime().before(new Date())
                && couponActivity.getEndTime().after(new Date())) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前优惠券是否有效
     * 当前是优惠券状态是生效、当前时间在优惠券可用范围内、
     */
    private boolean checkCouponIsAvailable(Coupon coupon) {
        return coupon.getStatus().equals(CouponStatusEnum.AVAILABLE.getCode())
                && coupon.getBeginTime().before(new Date())
                && coupon.getEndTime().after(new Date());
    }

    /**
     * 判断当前优惠券是否已过期
     * 当前是优惠券状态是生效、当前时间在优惠券可用范围内、
     */
    private boolean checkCouponIsExpired(Coupon coupon) {
        return coupon.getStatus().equals(CouponStatusEnum.EXPIRED.getCode())
                || coupon.getEndTime().before(new Date());
    }
}
