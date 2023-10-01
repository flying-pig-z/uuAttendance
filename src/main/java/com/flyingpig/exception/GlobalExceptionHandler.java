package com.flyingpig.exception;

import com.flyingpig.common.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局异常处理器
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result ex(Exception ex){
        ex.printStackTrace();
        if(ex instanceof AccessDeniedException){
            return Result.error("身份权限不符合");
        }
        return Result.error("对不起，操作失败，请联系管理员");
    }
}
