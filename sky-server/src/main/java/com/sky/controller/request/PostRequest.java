package com.sky.controller.request;

import lombok.Data;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/8 1:29
 */
@Data
public class PostRequest {
    private Integer id;
    private Integer userId;
    private Integer postTypeId; //直接从前端获取 123456
    private Integer goodsId;
    private Integer views;
    private String title;
    private String content;
    private String imageUrl; // 字符串列表形式 "["pg.png","pg2.png",...]"
    private List<String> imageList; //图片返回列表
}
