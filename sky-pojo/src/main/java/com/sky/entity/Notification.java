package com.sky.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/27 1:56
 */
@Data
@TableName("notification")
public class Notification {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    private Integer userId;
    private String title;
    private String type;//直接中文 促销通知、订单通知、系统维护通知
    private String messageContent;
    private boolean isRead;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
