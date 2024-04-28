package com.flyingpig.exception.handle;

import com.flyingpig.common.Result;
import com.flyingpig.exception.RateLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.BindException;

//全局异常处理器
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 请求参数异常/缺少--400
     *
     * @param e
     * @return
     */
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            BindException.class}
    )
    public Result missingServletRequestParameterException(Exception e) {
        return Result.error(400, "缺少参数或参数错误");
    }

    /**
     * 请求方法错误--405
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result httpRequestMethodNotSupportedExceptionHandler(Exception e){
        log.error("请求方法错误");
        return Result.error(405,"请求方法错误");
    }

    /**
     * 接口访问过于频繁--2
     * @param e
     * @return
     */
    @ExceptionHandler(RateLimitException.class)
    public Result rateLimitExceptionHandler(Exception e){
        log.error("接口访问过于频繁");
        return Result.error(2,"接口访问过于频繁");
    }

    /**
     * 全局异常处理
     * @param ex
     * @return
     */

    @ExceptionHandler(RedisConnectionFailureException.class)
    public Result redisConnectionFailureExceptionHandler(RedisConnectionFailureException ex) {
        log.error("redis未启动");
        return Result.error(2,"redis未启动");
    }

    /**
     * 全局异常处理
     * @param ex
     * @return
     */

    @ExceptionHandler(Exception.class)
    public Result ex(Exception ex) {
        ex.printStackTrace();
        if (ex instanceof AccessDeniedException) {
            return Result.error(0,"身份权限不符合");
        }
        return Result.error(2,"对不起，操作失败，请联系管理员");
    }
}
