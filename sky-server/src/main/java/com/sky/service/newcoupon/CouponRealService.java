package com.sky.service.newcoupon;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.controller.response.CouponNewResponse;
import com.sky.entity.CouponActivityReal;
import com.sky.entity.CouponTemplateReal;
import com.sky.entity.CouponUserReal;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.CouponActivityRealMapper;
import com.sky.mapper.CouponTemplateRealMapper;
import com.sky.mapper.UserCouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/25 0:07
 */
@Service
public class CouponRealService {

    @Autowired
    private CouponTemplateRealMapper couponTemplateRealMapper;

    @Autowired
    private CouponActivityRealMapper couponActivityRealMapper;

    @Autowired
    private UserCouponMapper userCoupon;

    // 创建优惠券
    @Transactional
    public void createCoupon(CouponTemplateReal couponTemplateReal){
         couponTemplateRealMapper.insert(couponTemplateReal);
    }

    // 获取所有优惠券模板
    public List<CouponTemplateReal> getCouponTemplates(Integer type) {
        if (type == null) {
            return couponTemplateRealMapper.selectList(null);
        } else {
            return couponTemplateRealMapper.selectList(new QueryWrapper<CouponTemplateReal>()
                    .eq("type", type));
        }
    }

    //启用 停用
    @Transactional
    public boolean updateCouponTemplateStatus(Integer templateId, boolean isActive) {
        if(templateId == null){
            throw new OrderBusinessException(MessageConstant.COUPON_ID_NULL);
        }
        CouponTemplateReal couponTemplate = couponTemplateRealMapper.selectById(templateId);
        if (couponTemplate != null) {
            couponTemplate.setIsActive(isActive);
            return couponTemplateRealMapper.updateById(couponTemplate) > 0;
        }
        return false;
    }


    // 创建优惠券活动
    @Transactional
    public void createCouponActivity(CouponActivityReal couponActivity) {
         couponActivity.setStatus(false);
         couponActivityRealMapper.insert(couponActivity);
    }

    @Transactional
    public boolean updateCouponActivityStatus(Integer actId, boolean isActive) {
        if(actId == null){
            throw new OrderBusinessException(MessageConstant.COUPON_ID_NULL);
        }
        CouponActivityReal couponActivityReal = couponActivityRealMapper.selectById(actId);
        if (couponActivityReal != null) {
            couponActivityReal.setStatus(isActive);
            return couponActivityRealMapper.updateById(couponActivityReal) > 0;
        }
        return false;
    }

    public List<CouponActivityReal> getCouponActivity(){
        return couponActivityRealMapper.selectList(null);
    }

    public boolean issueCouponToUser(Integer userId, Integer couponId) {
        // 这里可以根据实际业务逻辑进行发放优惠券的操作，比如更新用户优惠券表等
        return true;
    }

    //app 用戶领取优惠券
    @Transactional
    public void userClaimCoupon(List<Integer> couponIds) {
        int userId = BaseContext.getCurrentId().intValue();
        if(couponIds.isEmpty() || couponIds.size() == 0){
            throw new OrderBusinessException("领取失败");
        }
       for(Integer couponId : couponIds){
           if(couponId == null){
               throw new OrderBusinessException("领取失败");
           }
           CouponUserReal existingUserCoupon = userCoupon.selectOne(
                   new LambdaQueryWrapper<CouponUserReal>()
                           .eq(CouponUserReal::getUserId, userId)
                           .eq(CouponUserReal::getCouponId, couponId));
           if (existingUserCoupon!= null) {
               // 已经领取过，不能再次领取
//               throw new OrderBusinessException(MessageConstant.COUPON_GET_FAILED);
               continue;
           }

           //todo 读写锁 并发考虑
           CouponActivityReal activityReal = couponActivityRealMapper.selectById(couponId);
           CouponTemplateReal templateReal = couponTemplateRealMapper.selectById(activityReal.getTemplateId());

           int currentIssueNum = activityReal.getIssueNum() == null ? 0 : activityReal.getIssueNum();
           int maxIssueNum = templateReal.getMaxIssueNum();
           if (currentIssueNum >= maxIssueNum) {
               throw new OrderBusinessException("优惠券被领光了！请下次再来");
           }
           // 更新优惠券发放数量
           activityReal.setIssueNum(currentIssueNum + 1);
           couponActivityRealMapper.updateById(activityReal);

           // 检查优惠券是否可领取等逻辑
           CouponUserReal userReal = new CouponUserReal();
           userReal.setUserId(userId);
           userReal.setCouponId(couponId);
           userReal.setStatus(0); // 假设 0 代表未使用状态
           userReal.setReceiveTime(new Date());
           userCoupon.insert(userReal);
       }


    }

    public List<CouponNewResponse> getActCoupons(){
        List<CouponNewResponse> userCouponList = new ArrayList<>();
        List<CouponActivityReal> couponActivityReals = couponActivityRealMapper.selectList(null);
        /**
         *  目前设置所有商品都可以通用
         */
        for (CouponActivityReal activity :couponActivityReals) {
            CouponTemplateReal templateReal = couponTemplateRealMapper.selectById(activity.getTemplateId());
            if(activity.getStatus()){
                CouponNewResponse response = new CouponNewResponse();
                response.setCouponId(activity.getId());
                response.setActivityName(activity.getActivityName());
                response.setTemplateName(templateReal.getName());
                response.setType(templateReal.getType());
                response.setDiscountAmount(BigDecimal.valueOf(templateReal.getDiscountAmount()));
                response.setMinSpend(BigDecimal.valueOf(templateReal.getMinSpend()));
                response.setStartTime(templateReal.getStartTime());
                response.setEndTime(templateReal.getEndTime());
                userCouponList.add(response);
            }
        }
        return userCouponList;
    }

    public List<CouponNewResponse> getUserCoupons() {
        List<CouponUserReal> userReals = userCoupon.selectList(new LambdaQueryWrapper<CouponUserReal>()
                        .eq(CouponUserReal::getUserId, BaseContext.getCurrentId().intValue())
                        .eq(CouponUserReal::getStatus,0));
        // 需要返回的用户优惠券列表
        List<CouponNewResponse> userCouponList = new ArrayList<>();
        if(userReals.isEmpty() || userReals.size() == 0){
            return userCouponList;
        }

        for (CouponUserReal userReal : userReals) {
            CouponActivityReal activityReal = couponActivityRealMapper.selectById(userReal.getCouponId());
            CouponTemplateReal templateReal = couponTemplateRealMapper.selectById(activityReal.getTemplateId());
            if (templateReal!= null) {
                CouponNewResponse response = new CouponNewResponse();
                response.setId(userReal.getId());
                response.setUserId(userReal.getUserId());
                response.setCouponId(userReal.getCouponId());
                // 设置其他从模板获取的属性
                response.setActivityName(activityReal.getActivityName()); // 根据实际情况设置活动名称
                response.setTemplateName(templateReal.getName());
                response.setType(templateReal.getType());
                response.setDiscountAmount(BigDecimal.valueOf(templateReal.getDiscountAmount()));
                response.setMinSpend(BigDecimal.valueOf(templateReal.getMinSpend()));
                response.setStatus(userReal.getStatus());
                response.setStartTime(templateReal.getStartTime());
                response.setEndTime(templateReal.getEndTime());
                response.setReceiveTime(userReal.getReceiveTime());
                response.setUseTime(userReal.getUseTime());
                response.setOrderId(userReal.getOrderId());
                userCouponList.add(response);
            }
        }
        return userCouponList;
    }

    public void redeemCoupon(Integer couponId, Integer orderId) {
        // 更新优惠券状态为已使用等逻辑
        CouponUserReal real = userCoupon.selectById(couponId);
        if (real != null && real.getStatus() == 0) {
            real.setStatus(CouponUserReal.VERIFY_USED); // 假设 1 代表已使用状态
            real.setUseTime(new Date());
            real.setOrderId(orderId);
            userCoupon.updateById(real);
        }else{
            throw new OrderBusinessException(MessageConstant.COUPON_STS_FAILED);
        }
    }

    public CouponUserReal getCouponStatus(Integer couponId) {
        return userCoupon.selectById(couponId);
    }
}
