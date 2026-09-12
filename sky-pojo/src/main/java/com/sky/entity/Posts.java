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
 * @date:2024/10/6 3:05
 */
@Data
@TableName("posts")
public class Posts {

    public static Integer TYPE_LATEST = 0;

    public static Integer MOM_EXPERIENCE = 1;
    public static Integer PARENTING_KNOWLEDGE = 2;
    public static Integer SPORTS_PERSON  = 3;
    public static Integer SCIENTIFIC_KNOWLEDGE  = 4;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer postTypeId; //直接从前端获取 123456
    private Integer goodsId;
    private Integer views;
    private String title;
    private String content;
    private String imageUrl; // 字符串列表形式 "[,,]"
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;
    private Integer likeNum;
    private Integer commentNum;
//    List<String> imageList  图片返回列表
}
