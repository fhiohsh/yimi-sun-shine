package com.sky.controller.user;

import com.sky.controller.request.PostRequest;
import com.sky.controller.response.PostResponse;
import com.sky.entity.Comments;
import com.sky.entity.Posts;
import com.sky.result.Result;
import com.sky.service.PostService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/8 1:57
 */
@RestController
@RequestMapping("/user/post")
@Slf4j
@Api(tags = "贴子相关接口")
public class PostController {

    @Autowired
    private PostService postService;

    // 获取帖子列表
    @GetMapping("/list/{type}")
    public Result<List<PostResponse>> getPostList(@PathVariable(value = "type") Integer type) {
        return Result.success(postService.getPostListDeep(type));
    }

    @GetMapping("/detail/{postId}")
    public Result<PostResponse> getPostDetail(@PathVariable(value = "postId") Integer postId) {
        return Result.success(postService.getPostDetail(postId));
    }

    // 发布帖子
    @PostMapping("/add")
    public Result<?> publishPost(@RequestBody PostRequest post) {
        return Result.success(postService.publishPost(post));
    }

    // 点赞帖子
    @PostMapping("/likes")
    public Result<?> likePost(@RequestBody PostRequest post) {
        postService.likePost(post);
        return Result.success();
    }

    // 取消点赞
    @PostMapping("/cancel/likes")
    public Result<?> likeCancelPost(@RequestBody PostRequest post) {
        postService.cancelLikePost(post);
        return Result.success();
    }

    // 评论帖子
    @PostMapping("/comment")
    public Result<?> commentPost(@RequestBody Comments postComment) {
        postService.commentPost(postComment);
        return Result.success();
    }


}
