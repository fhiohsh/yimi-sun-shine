package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌 Token
        String token = request.getHeader(jwtProperties.getAdminTokenName());

//        if (!StringUtils.hasText(token)) {
//            log.info("{}", "无token,请登录。");
//            //放行
//           response.setStatus();
//            return;
//        }
        //2、校验令牌
        try {
//            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
//            log.info("当前操作请求-员工id：{}",empId);
            //存入当前线程内
            BaseContext.setCurrentId(empId);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
//            log.info("jwt过期");
            response.setStatus(401);
            return false;
        }
        //todo redis校验

        // 在登录时 redis存放 => redisTemplate.opsForValue().set("tk" + userId, user,60, TimeUnit.MINUTES);
        // 使用redis做校验
        //  Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
        //            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
        //String redisKey = "tk" + userId;
        //        UserDT user = (UserDT) redisTemplate.opsForValue().get(redisKey);

        ////        续期 离过期时间只有20分钟时才更新过期时间
        //        if ( redisTemplate.opsForValue().getOperations().getExpire(redisKey) < 1 * 60 * 20) {
        //            redisTemplate.opsForValue().set("tk" + userId, user,60, TimeUnit.MINUTES);
        //            log.error("update token info, id is:{}, user info is:{}", user.getUser().getId(), user.getUser());
        //        }

    }
}