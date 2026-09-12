package com.sky.controller.admin;

import com.sky.entity.Article;
import com.sky.entity.Notification;
import com.sky.result.Result;
import com.sky.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/10/2 16:18
 */
@RestController
@RequestMapping("/admin/notification")
@Slf4j
@Api(tags = "消息通知相关接口")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/getMessage")
    @ApiOperation("获取列表消息")
    public Result<List<Notification>> getMessage(){
        return Result.success(notificationService.getAllMessage());
    }

    @PostMapping("/add")
    @ApiOperation("发布消息")
    public Result<?> addNotification(@RequestBody Notification notification) {
        notificationService.sendMsg(notification);
        return Result.success();
    }
}
