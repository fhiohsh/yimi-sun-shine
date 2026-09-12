package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.controller.request.UserRequest;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.exception.UserNotLoginException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author: Joey
 * @Description:
 * @date:2024/6/1 14:38
 */
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    private static final String jscode2sessionUrl = "https://api.weixin.qq.com/sns/jscode2session";

    @Transactional
    public User wxLogin(UserLoginDTO dto) throws IOException {
        //获取openID
        JSONObject Info = getOpenId(dto.getCode());
        String openid = Info.getString("openid");
        //置空判断
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //新用户注册
        User user = userMapper.queryOpenId(openid);
        if(user == null){
            user = User.builder()
                    .openId(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insertUser(user);
        }
        //返回
        return user;
    }

    @Override
    @Transactional
    public void sendVerificationCode(UserLoginDTO dto) {
        //验证
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("phone",dto.getPhone());
        User user = userMapper.selectOne(userWrapper);
        if (user == null) {
            throw new UserNotLoginException("手机号不存在");
        }
        Random random = new Random();
        int verificationCode = 1000 + random.nextInt(9000);
        user.setVerifyCode(String.valueOf(verificationCode));
        int update = userMapper.updateById(user);
        if(update <= 0){
            throw new UserNotLoginException("手机号错误");
        }
        System.out.println("用户手机号:"+user.getPhone()+" 验证码====> "+ verificationCode);
    }

    @Override
    public User phoneLogin(UserLoginDTO dto) {
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("phone",dto.getPhone());
        User user = userMapper.selectOne(userWrapper);
        if(!user.getVerifyCode().equals(dto.getPhoneCode())) {
            throw new UserNotLoginException("验证码错误");
        }
        return user;
    }

    private JSONObject getOpenId(String code) throws IOException {
        Map<String, String> requestUrlParam = new HashMap<>();
        requestUrlParam.put("js_code", code);
        requestUrlParam.put("appid", weChatProperties.getAppid());
        requestUrlParam.put("secret", weChatProperties.getSecret());
        requestUrlParam.put("grant_type", "authorization_code");
        // https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=ACCESS_TOKEN
        return JSON.parseObject(HttpClientUtil.doPost(jscode2sessionUrl, requestUrlParam));
    }

    @Override
    public void updateUserInfo(UserRequest userRequest) {
        if(userRequest.getUserId() != BaseContext.getCurrentId().intValue() || userRequest.getUserId() == null){
            throw new UserNotLoginException("用户信息异常");
        }
        User user = userMapper.selectById(userRequest.getUserId());
        if(userRequest.getAvatar()!=null){
            user.setAvatar(userRequest.getAvatar());
        }
        if(userRequest.getIntroduction()!=null){
            user.setIntroduction(userRequest.getIntroduction());
        }
        if(userRequest.getUserName()!=null){
            user.setUserName(userRequest.getUserName());
        }
        userMapper.updateById(user);
    }
}
