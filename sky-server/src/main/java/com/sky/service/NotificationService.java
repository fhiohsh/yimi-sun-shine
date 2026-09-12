package com.sky.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.j2objc.annotations.AutoreleasePool;
import com.sky.context.BaseContext;
import com.sky.entity.Notification;
import com.sky.entity.Orders;
import com.sky.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/27 2:14
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    public List<Notification> getMessage(){
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", BaseContext.getCurrentId()).orderByAsc("create_time");
        List<Notification> list1 = notificationMapper.selectList(queryWrapper);
        QueryWrapper<Notification> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.isNull("user_id").orderByAsc("create_time");
        List<Notification> list2 = notificationMapper.selectList(queryWrapper2);

        List<Notification> combinedList = new ArrayList<>();
        combinedList.addAll(list2);
        combinedList.addAll(list1);

        Collections.sort(combinedList, new Comparator<Notification>() {
            @Override
            public int compare(Notification n1, Notification n2) {
                return n2.getCreateTime().compareTo(n1.getCreateTime());
            }
        });

        return combinedList;
    }

    public List<Notification> getAllMessage(){
        return notificationMapper.selectList(null);
    }

    @Transactional
    public void sendMsg(Notification notification){
        notification.setType("系统通知");
        notificationMapper.insert(notification);
    }

    @AutoreleasePool
    public void setIsRead(Integer msgId){
        Notification notification = notificationMapper.selectById(msgId);
        notification.setRead(true);
        notificationMapper.updateById(notification);
    }
}
