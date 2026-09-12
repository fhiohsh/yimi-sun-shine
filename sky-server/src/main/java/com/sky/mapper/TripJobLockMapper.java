package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Employee;
import com.sky.entity.TripJobLock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TripJobLockMapper extends BaseMapper<TripJobLock> {
    @Select("SELECT is_lock FROM trip_job_lock WHERE job_name = #{jobName}")
    String getIsLock(String jobName);

    @Select("SELECT job_cron FROM trip_job_lock WHERE job_name = #{jobName}")
    String getCron(String jobName);

}
