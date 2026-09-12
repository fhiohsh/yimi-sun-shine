package com.sky.controller.user;

import com.sky.entity.Notification;
import com.sky.result.Result;
import com.sky.service.NotificationService;
import com.sky.vo.OrderDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/27 2:12
 */
@RestController
@RequestMapping("/user/notification")
@Slf4j
@Api(tags = "消息通知相关接口")
public class NotificationUserController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/getMessage")
    @ApiOperation("获取用户消息")
    public Result<List<Notification>> getMessage(){
        return Result.success(notificationService.getMessage());
    }

    //setIsRead
    @PostMapping("/read/{msg_id}")
    @ApiOperation("消息已读")
    public Result<String> getMessage(@PathVariable(value = "msg_id") Integer msgId){
        notificationService.setIsRead(msgId);
        return Result.success();
    }

}
