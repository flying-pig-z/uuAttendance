package com.flyingpig.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int value(); // 指定时间窗口内允许的访问次数
    long time() default 60; // 时间窗口大小，单位为秒

}
