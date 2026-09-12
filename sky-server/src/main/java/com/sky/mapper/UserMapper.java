package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where open_id = #{openId}")
    User queryOpenId(String openId);

    int insertUser(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);


}
