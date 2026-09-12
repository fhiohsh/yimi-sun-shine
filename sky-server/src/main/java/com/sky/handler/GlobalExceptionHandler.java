package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice  //统一异常处理
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param e
     * @return
     */
    @ExceptionHandler
    public Result<?> exceptionHandler(BaseException e){
        log.error("异常信息：{}", e.getMessage());
        if (e instanceof BaseException) {
            return Result.error(e.getMessage());
        } else {
            // 处理其他未预期的异常
            return Result.error("系统出现未知错误");
        }
    }

    /*
    *   重复姓名异常抛出
    *  */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
    //eg: SQLIntegrityConstraintViolationException: Duplicate entry 'lisi' for key 'employee.idx_username'
        //获取异常信息
        String exMessage = ex.getMessage();
        if(exMessage.contains("Duplicate entry")){
            String[] split = exMessage.split(" ");
            String userName = split[2];
            String msg = userName + MessageConstant.ACCOUNT_ALREADY_EXIST;
            return Result.error(msg);
        }else{
            return Result.error("未知错误");
        }

    }

}
