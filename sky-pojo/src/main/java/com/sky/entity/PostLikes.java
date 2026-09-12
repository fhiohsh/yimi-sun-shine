package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/6 3:09
 */
@Data
@TableName("post_likes")
public class PostLikes {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer postId;

    private Integer userId;

    private Boolean likeStatus;

    private Date createTime;
}
