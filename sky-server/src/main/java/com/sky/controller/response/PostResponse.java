package com.sky.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sky.entity.Comments;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/8 2:05
 */
@Data
public class PostResponse {
    private Integer id;
    private Integer userId;
    private String userAvatar;
    private String userName;
    private Integer postTypeId; //直接从前端获取 123456

    private String title;
    private String content;
    private String imageUrl; // 字符串列表形式 "[,,]"
    private List<String> imageList; //图片返回列表

    private Integer views;
    private Integer likeNum;
    private Integer commentNum;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    private Integer goodsId;
    private String goodsName;
    private Integer goodsPrice;

    private List<CommentResponse> commentsList;//评论列表

}
