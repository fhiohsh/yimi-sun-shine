package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/25 0:01
 */

@TableName("coupon_activity")
@Data
public class CouponActivityReal {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer templateId;
    private String activityName;
    private Boolean status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Integer issueNum;
    private String targetUsers;
}
