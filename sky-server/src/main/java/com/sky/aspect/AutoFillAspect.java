package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: Joey
 * @Description: 自动添加
 * @date:2024/4/12 23:39
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    //切入点 仅扫描mapper包和注解
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /*
    *   前置通知 方法执行前操作
    * */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        //获取操作类型
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = sign.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        //获取当前方法对象
        Object[] args = joinPoint.getArgs();

        if(args == null || args.length ==0){ //置空判断
            return;
        }

        Object entity = args[0];//约定中第一个参数为方法对象实体
        //数据
//        LocalDateTime now = LocalDateTime.now();
        Date now = new Date();
        Long currentId = BaseContext.getCurrentId();

        //判断类型 反射赋值
        if(operationType == OperationType.INSERT){
            try{
                Method setCreatTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, Date.class);
//                Method setCreatUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, Date.class);
//                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                //反射赋值 类加载
                setCreatTime.invoke(entity,now);
//                setCreatUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
//                setUpdateUser.invoke(entity,currentId);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(operationType == OperationType.UPDATE){
            try{
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,Date.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
