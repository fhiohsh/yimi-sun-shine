package com.sky.controller.admin;

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
 * @date:2024/10/1 23:55
 */
@RestController
@RequestMapping("/admin/article")
@Slf4j
@Api(tags = "文章资讯接口")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/add")
    public Result<?> addArticle(@RequestBody Article article) {
        articleService.addArticle(article);
        return Result.success();
    }

    @PostMapping("/query")
    public Result<List<ArticleResponse>> getArticle() {
        return Result.success(articleService.getList(null));
    }


}
