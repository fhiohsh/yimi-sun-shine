package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.CouponActivityLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description TODO
 * @Author eugene
 * @Data 2023/4/7 21:24
 */
@Mapper
public interface CouponActivityLogMapper extends BaseMapper<CouponActivityLog> {
}
