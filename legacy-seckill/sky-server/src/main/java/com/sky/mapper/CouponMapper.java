package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Coupon;
import com.sky.entity.CouponActivity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {

//    @Insert("insert into t_coupon_activity (name, coupon_template_code, total_number, limit_number, status, begin_time, end_time, create_time, update_time) " +
//            "VALUES (#name,#couponTemplateCode,#totalNumber,#limitNumber,#status,#beginTime,#beginTime,#endTime,#createTime,#updateTime)")
//    int insertActivity(CouponActivity couponActivity);
}
