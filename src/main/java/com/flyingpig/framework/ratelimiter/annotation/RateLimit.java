package com.flyingpig.framework.ratelimiter.annotation;



import com.flyingpig.framework.ratelimiter.model.Mode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author flyingpig
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RateLimit {

    //===================== 公共参数 ============================

    Mode mode() default Mode.FIXED_WINDOW;

    /**
     * Spring EL表达式指定具体参数，也可以通过@RateLimitKey注解指定
     * 默认是全类名.方法名
     * @return keys
     */
    String [] keys() default {};



    //===================== 时间窗口/滑动窗口模式参数 ============================

    /**
     * 时间窗口模式表示每个时间窗口内的请求数量
     * @return limitNum
     */
    int limitNum() default 1;

    /**
     * 窗口大小，最小单位秒，如 2s，2h , 2d ,默认 1s
     * @return windowSize
     */
    String windowSize() default "1s";


    //===================== 令牌桶模式参数 ============================

    /**
     * 令牌桶容量,默认为1,这个用来应对瞬时最大并发数量
     * @return bucketCapacity
     */
    int bucketCapacity() default 1;

    /*
     *  每秒令牌的生成数量，这个是限制流量的关键
     */
    int generateTokenRate() default 1;

    /**
     * 每次请求消耗多少令牌，默认为1，用处似乎不大，因为消耗数量和生成数量调节一个就行了
     * @return requestedTokens
     */
    int consumeTokenPerReq() default 1;

}