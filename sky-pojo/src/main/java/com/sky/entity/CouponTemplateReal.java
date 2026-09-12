package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/24 23:58
 */
@Data
@TableName("coupon_template")
public class CouponTemplateReal {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private Integer type; // 1满减券、2折扣券、3无门槛券
    private Double discountAmount;
    private Double minSpend;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Boolean isActive;
    private Integer maxIssueNum;
    private Integer usageScope; //单品、品类、全场
}
