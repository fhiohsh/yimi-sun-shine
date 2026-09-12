package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/24 0:25
 */
@RestController
@RequestMapping("/admin")
@Slf4j
@Api(tags = "token")
public class TokenController {

    @GetMapping("/verifyToken")
    public Result verifyToken(HttpServletRequest request) {
        // 这里获取请求中的 token，假设请求头中携带 Authorization 字段
        String token = request.getHeader("Token");
        // 进行 token 的验证逻辑，例如与数据库中的有效 token 进行对比等
        if (token != null) {
            return Result.success();
        }else{
            log.info("jwt过期");
            return Result.error("登录失效，请重新登录");
        }

    }

    private boolean isValidToken(String token) {
        // 这里可以根据实际情况实现 token 的有效性验证逻辑
        // 例如检查 token 是否过期、是否与数据库中的记录匹配等
        return false;
    }
}
