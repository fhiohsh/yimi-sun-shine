package com.sky.controller.user;

import com.sky.controller.response.ArticleResponse;
import com.sky.entity.Article;
import com.sky.result.Result;
import com.sky.service.ArticleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/2 0:44
 */

@RestController
@RequestMapping("/user/article")
@Slf4j
@Api(tags = "用户文章资讯接口")
public class ArticleUserController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/query")
    public Result<List<ArticleResponse>> getArticle() {
        return Result.success(articleService.getList(null));
    }

    @GetMapping("/query/{id}")
    public Result<ArticleResponse> getArticleById(@PathVariable(value = "id") Integer id) {
        return Result.success(articleService.getList(id).get(0));
    }
}
