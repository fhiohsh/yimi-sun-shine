package com.sky.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/11 0:35
 */
@Data
public class CommentResponse {
    private Integer id;
    private Integer parentId;
    private Integer productId;
    private Integer postId;
    private Integer userId;
    private String userName;
    private Integer commentType;
    private String commentContent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;
    private List<CommentResponse> replies;
}
