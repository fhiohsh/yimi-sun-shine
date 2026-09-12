package com.sky.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.context.BaseContext;
import com.sky.controller.request.PostCommentRequest;
import com.sky.controller.request.PostRequest;
import com.sky.controller.response.CommentResponse;
import com.sky.controller.response.PostResponse;
import com.sky.entity.*;
import com.sky.exception.BaseException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/6 3:13
 */
@Service
public class PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostLikesMapper postLikeMapper;

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UserMapper userMapper;


    public List<PostResponse> getPostListDeep(Integer type){
        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        if (type != null && type != 0) {
            queryWrapper.eq("post_type_id", type);
        }
        // 查询所有最新类型的帖子
        queryWrapper.orderByDesc("create_time");
        List<Posts> posts = postMapper.selectList(queryWrapper);

        // 查询所有帖子的评论数
        Map<Integer, Integer> commentCounts = commentsMapper.selectList(new LambdaQueryWrapper<>())
                .stream()
                .collect(Collectors.groupingBy(Comments::getPostId, Collectors.summingInt(c -> 1)));

        // 查询所有帖子的点赞数
        Map<Integer, Integer> likeCounts = postLikeMapper.selectList(new LambdaQueryWrapper<>())
                .stream()
                .collect(Collectors.groupingBy(PostLikes::getPostId, Collectors.summingInt(l -> 1)));

        //添加浏览量
        // 构建响应列表
        return posts.stream()
                .map(pt -> {
                    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, pt.getUserId()));
                    PostResponse postResponse = new PostResponse();
                    postResponse.setId(pt.getId());
                    postResponse.setUserId(pt.getUserId());
                    postResponse.setUserAvatar(user.getAvatar());
                    postResponse.setUserName(user.getUserName());
                    postResponse.setPostTypeId(pt.getPostTypeId());
                    postResponse.setGoodsId(pt.getGoodsId());
                    postResponse.setViews(pt.getViews());
                    postResponse.setTitle(pt.getTitle());
                    postResponse.setContent(pt.getContent());
                    postResponse.setImageUrl(pt.getImageUrl());
                    postResponse.setCreateTime(pt.getCreateTime());
                    //todo 评论数和点赞暂时手动
                    postResponse.setCommentNum(commentCounts.getOrDefault(pt.getId(), 0));
                    postResponse.setLikeNum(likeCounts.getOrDefault(pt.getId(), 0));
//                    postResponse.setCommentNum(pt.getCommentNum());
//                    postResponse.setLikeNum(pt.getLikeNum());
                    return postResponse;
                })
                .collect(Collectors.toList());
    }

    public PostResponse getPostDetail(Integer postId){
        PostResponse postResponse = new PostResponse();
        Posts posts = postMapper.selectById(postId);
        //获取商品
        Goods goods = goodsMapper.selectById(posts.getGoodsId());
        postResponse.setGoodsName(goods.getName());
        postResponse.setGoodsPrice(goods.getPrice().intValue());
        //评论列表
        List<CommentResponse> commentsList = getCommentsByPostId2(postId);
        postResponse.setCommentsList(commentsList);
        posts.setViews(posts.getViews() + 1);
        postMapper.updateById(posts);
        return postResponse;
    }

    private List<CommentResponse> getCommentsByPostId(Integer postId){
        List<Comments> commentsList = commentsMapper.selectList(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getPostId, postId));

        List<CommentResponse> commentResponses = new ArrayList<>();
        commentsList.forEach(item -> {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, item.getUserId()));
            //一级评论
            CommentResponse commentResponse = new CommentResponse();
            BeanUtils.copyProperties(item, commentResponse);
            //子评论
            List<CommentResponse> replyList = new ArrayList<>();
            commentsList.forEach(itemChild -> {
                if(itemChild.getParentId() != null && itemChild.getParentId().equals(item.getId())){
                    CommentResponse childComments = new CommentResponse();
                    BeanUtils.copyProperties(itemChild, childComments);
                    replyList.add(childComments);
                }
            });
            commentResponse.setReplies(replyList);
            commentResponse.setUserName(user.getUserName());
            commentResponses.add(commentResponse);
        });
        return commentResponses;
    }


    private List<CommentResponse> getCommentsByPostId2(Integer postId){
        List<Comments> commentsList = commentsMapper.selectList(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getPostId, postId));
        // 创建一个Map来存储所有评论，键为评论ID，值为CommentResponse对象
        Map<Integer, CommentResponse> commentMap = new HashMap<>();
        List<CommentResponse> commentResponses = new ArrayList<>();
        // 遍历所有评论，构建评论映射
        commentsList.forEach(item -> {
            CommentResponse commentResponse = new CommentResponse();
            BeanUtils.copyProperties(item, commentResponse);

            // 获取用户信息并设置用户名
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, item.getUserId()));
            commentResponse.setUserName(user.getUserName());

            // 将评论存入Map
            commentMap.put(item.getId(), commentResponse);
        });

        // 再次遍历所有评论，这次是为了处理父子关系
        commentsList.forEach(item -> {
            CommentResponse commentResponse = commentMap.get(item.getId());

            // 如果该评论有父评论
            if (item.getParentId() != null) {
                // 找到父评论，并将当前评论添加到父评论的回复列表中
                CommentResponse parentComment = commentMap.get(item.getParentId());
                if (parentComment != null) {
                    List<CommentResponse> replies = parentComment.getReplies();
                    if (replies == null) {
                        replies = new ArrayList<>();
                        parentComment.setReplies(replies);
                    }
                    replies.add(commentResponse);
                }
            } else {
                // 如果是顶级评论，则添加到最终结果列表中
                commentResponses.add(commentResponse);
            }
        });
        return commentResponses;
    }


    //发布贴子
    @Transactional
    public Integer publishPost(PostRequest req) {
        //todo 转为string
        //req.getImageList();
        Posts post=new Posts();
        BeanUtils.copyProperties(req,post);
        post.setUserId(BaseContext.getCurrentId().intValue());
        post.setViews(1);
        post.setPostTypeId(req.getPostTypeId() == null ? 1 : req.getPostTypeId());
        post.setCreateTime(new Date());
        post.setLikeNum(1);
        post.setCommentNum(1);
        if(postMapper.insert(post) > 0){
            return post.getId();
        }else{
            throw new OrderBusinessException("贴子信息发布发生错误");
        }
    }


    //点赞
    @Transactional
    public void likePost(PostRequest req) {
        Posts posts = postMapper.selectById(req.getId());
        if(posts == null){
            throw new BaseException("贴子不存在");
        }
        PostLikes selectOne = postLikeMapper.selectOne(new LambdaQueryWrapper<PostLikes>()
                .eq(PostLikes::getPostId, req.getId())
                .eq(PostLikes::getUserId, BaseContext.getCurrentId()));
        if (selectOne != null) {
            throw new BaseException("请勿重复点赞");
        }

        PostLikes postLikes = new PostLikes();
        postLikes.setPostId(req.getId());
        postLikes.setUserId(BaseContext.getCurrentId().intValue());
        postLikes.setLikeStatus(true);
        postLikes.setCreateTime(new Date());
        postLikeMapper.insert(postLikes);

        Integer selectCount = postLikeMapper.selectCount(new LambdaQueryWrapper<PostLikes>().eq(PostLikes::getPostId, req.getId()));
        posts.setLikeNum(selectCount);
        postMapper.updateById(posts);
    }

    //取消点赞
    @Transactional
    public void cancelLikePost(PostRequest req) {
        Posts posts = postMapper.selectById(req.getId());
        if(posts == null){
            throw new BaseException("贴子不存在");
        }
        postLikeMapper.delete(new LambdaQueryWrapper<PostLikes>()
                .eq(PostLikes::getPostId, req.getId())
                .eq(PostLikes::getUserId, BaseContext.getCurrentId()));
        Integer selectCount = postLikeMapper.selectCount(new LambdaQueryWrapper<PostLikes>()
                .eq(PostLikes::getPostId, req.getId()));
        posts.setLikeNum(selectCount);
        postMapper.updateById(posts);
    }

    //评论
    @Transactional
    public void commentPost(Comments req) {
        Posts posts = postMapper.selectById(req.getPostId());
        if(req.getCommentType() != 2) {
            throw new BaseException("评论失败");
        }

        req.setCreateTime(new Date());
        req.setUserId(BaseContext.getCurrentId().intValue());
        commentsMapper.insert(req);

        Integer selectCount = commentsMapper.selectCount(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getPostId, req.getId()));
        posts.setCommentNum(selectCount);
        postMapper.updateById(posts);
    }

//    @Transactional
//    public void reply(Comments req){
//        if (req.getParentId() == null) {
//            throw new BaseException("发生错误");
//        }
//
//    }


}
