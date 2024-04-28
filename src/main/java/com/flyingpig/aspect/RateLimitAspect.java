package com.flyingpig.aspect;

import com.flyingpig.annotations.UserRateLimit;
import com.flyingpig.dataobject.dto.LoginUser;
import com.flyingpig.exception.RateLimitException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

/*
    接口根据用户访问频率限流切面
*/
@Aspect
@Component
public class RateLimitAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Before("@annotation(rateLimit) && args(arg,..)")
    public void rateLimit(JoinPoint joinPoint, UserRateLimit rateLimit, Object arg) throws Exception {
        String methodName = joinPoint.getSignature().toShortString();
        String key = "rate_limit:" + methodName; // 使用参数作为限流的键
        long limit = rateLimit.value();
        long expireTime = rateLimit.time();

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/rate_limit.lua")));

        // 执行 Lua 脚本
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), limit + "", expireTime + "");

        if (result != null && result == 0) {
            throw new RateLimitException("访问频率超限");
        }
    }
}
