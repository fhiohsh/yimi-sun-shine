package com.sky.controller.request;

import lombok.Data;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/8 1:48
 */
@Data
public class PostCommentRequest {
    private Integer id;
    private Integer parentId;
    private Integer productId;
    private Integer postId;
    private Integer userId;
    private Integer commentType;
    private String commentContent;
}
