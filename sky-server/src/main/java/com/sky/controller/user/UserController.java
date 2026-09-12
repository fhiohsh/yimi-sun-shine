package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.controller.request.UserRequest;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Joey
 * @Description:
 * @date:2024/6/1 14:27
 */
@RestController
@RequestMapping("/user/user")
@Slf4j
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;


    @PostMapping("/login")
    @ApiOperation("微信授权登录")
    public Result<UserLoginVO> login(String code) throws IOException {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setCode(code);
        User user = userService.wxLogin(loginDTO);
        UserLoginVO vo = generateJwt(user);
        return Result.success(vo);
    }

    @PostMapping("/send/verify")
    @ApiOperation("发送验证码")
    public Result<String> sendCode(@RequestBody UserLoginDTO loginDTO) {
        userService.sendVerificationCode(loginDTO);
        return Result.success();
    }

    @PostMapping("/login/phone")
    @ApiOperation("手机号登录")
    public Result<UserLoginVO> phoneLogin(@RequestBody UserLoginDTO loginDTO) {
        User user = userService.phoneLogin(loginDTO);
        //JWT令牌生成
        UserLoginVO vo = generateJwt(user);
        return Result.success(vo);
    }

    @PostMapping("/update")
    @ApiOperation("用户更新信息")
    public Result<String> updateUserInfo(@RequestBody UserRequest userRequest) {
       userService.updateUserInfo(userRequest);
        return Result.success();
    }

    public UserLoginVO generateJwt(User user){
        Map<String, Object> phoneClaims = new HashMap<>();
        phoneClaims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey()
                ,jwtProperties.getUserTtl()
                ,phoneClaims);
        return UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenId())
                .userName(user.getUserName())
                .avatar(user.getAvatar())
                .introduction(user.getIntroduction())
                .token(token)
                .build();
    }

}
