package com.sky.controller.response;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/2 0:04
 */
@Data
public class ArticleResponse {
    private Integer id;
    //主标题
    private String mainTitle;
    //副标题
    private String subTitle;
    //图片路径url
    private String image;
    //内容
    private String content;

    private Integer viewNum;
    // 1 开启  0关闭
    private Integer status;
    //关联商品id   Goods relatedProduct = new Goods();
    private String relatedProductName;
    private Integer relatedProductId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
}
