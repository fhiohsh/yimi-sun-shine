package com.sky.service.coupon;

import com.sky.controller.request.CouponTemplateRequest;
import com.sky.entity.CouponTemplate;
import com.sky.enumeration.StatusEnum;
import com.sky.mapper.CouponTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.sky.utils.CouponUtil.getCouponTemplateCode;

/**
 * @author: Joey
 * @Description:
 * @date:2024/7/18 17:31
 */
@Service
public class CouponTemplateServiceImp implements CouponTemplateService {

    @Autowired
    private CouponTemplateMapper couponTemplateMapper;
    @Autowired
    private TemplateCacheService templateCacheService;

    @Override
    public boolean addCouponTemplate(CouponTemplateRequest request) {
        CouponTemplate couponTemplate = new CouponTemplate();
        //自动生成code
        couponTemplate.setCode(getCouponTemplateCode());
        couponTemplate.setName(request.getName());
        couponTemplate.setPrice(request.getPrice());
        couponTemplate.setLimitNumber(request.getLimitNumber());
        couponTemplate.setLimitSku(request.getLimitSku());
        couponTemplate.setLimitSpu(request.getLimitSpu());
        couponTemplate.setValidityType(request.getValidityType());
        couponTemplate.setBeginTime(request.getBeginTime());
        couponTemplate.setEndTime(request.getEndTime());
        couponTemplate.setValidityDay(request.getValidityDay());
        couponTemplate.setStatus(StatusEnum.AVAILABLE.getCode());
        couponTemplate.setCreateTime(new Date());
        couponTemplate.setUpdateTime(new Date());
        int result = couponTemplateMapper.insert(couponTemplate);
        if (result > 0) {
            // 保存到Guava缓存中
            templateCacheService.setCouponTemplateCache(couponTemplate.getCode(), couponTemplate);
        }
        return result > 0;
    }
}
