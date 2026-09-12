package com.sky.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: Joey
 * @Description: 资讯文章
 * @date:2024/10/1 21:28
 */
@Data
@TableName("article")
public class Article {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    //主标题
    private String mainTitle;
    //副标题
    private String subTitle;
    //图片路径url
    private String image;
    //内容
    private String content;
    //浏览量
    private Integer viewNum;
    // 1 开启  0关闭
    private Integer status;
    //关联商品id   Goods relatedProduct = new Goods();
    private Integer relatedProductId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
