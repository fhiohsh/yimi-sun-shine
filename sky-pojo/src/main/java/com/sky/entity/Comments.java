package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/6 3:03
 */
@Data
@TableName("comments")
public class Comments {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private Integer productId;
    private Integer postId;
    private Integer userId;
    private Integer commentType;
    private String commentContent;
    private Date createTime;
}
