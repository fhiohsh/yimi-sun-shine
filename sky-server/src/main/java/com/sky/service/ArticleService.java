package com.sky.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.controller.response.ArticleResponse;
import com.sky.entity.Article;
import com.sky.entity.Goods;
import com.sky.entity.Orders;
import com.sky.mapper.ArticleMapper;
import com.sky.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/1 21:38
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private GoodsMapper goodsMapper;


    @Transactional
    public void addArticle(Article article){
        articleMapper.insert(article);
    }


    public List<ArticleResponse> getList(Integer id){
        QueryWrapper<Article> ArticleQueryWrapper = new QueryWrapper<>();
        ArticleQueryWrapper.eq("id", id);

        List<ArticleResponse> responseList = new ArrayList<>();
        List<Article> articleList;
        if(id != null){
            articleList = articleMapper.selectList(ArticleQueryWrapper);
        }else{
            articleList = articleMapper.selectList(null);
        }
        if(articleList.size() != 0){
            articleList.forEach(item -> {
                ArticleResponse articleResponse = new ArticleResponse();
                articleResponse.setId(item.getId());
                articleResponse.setContent(item.getContent());
                articleResponse.setCreateTime(item.getCreateTime());
                articleResponse.setId(item.getId());
                articleResponse.setMainTitle(item.getMainTitle());
                articleResponse.setSubTitle(item.getSubTitle());
                articleResponse.setImage(item.getImage());
                articleResponse.setStatus(item.getStatus());
                articleResponse.setViewNum(item.getViewNum());
                Goods goods = goodsMapper.queryGoods(item.getRelatedProductId().longValue());
                articleResponse.setRelatedProductName(goods.getName());
                articleResponse.setRelatedProductId(item.getRelatedProductId());
                responseList.add(articleResponse);
            });
        }

        return responseList;
    }
}
