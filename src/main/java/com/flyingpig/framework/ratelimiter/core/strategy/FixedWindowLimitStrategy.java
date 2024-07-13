package com.flyingpig.framework.ratelimiter.core.strategy;

import com.flyingpig.framework.ratelimiter.util.LuaScriptSelector;
import com.flyingpig.framework.ratelimiter.model.Rule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
    固定窗口算法
 */
@Component
@RequiredArgsConstructor
public class FixedWindowLimitStrategy implements RateLimitStrategy {

    private final StringRedisTemplate redisTemplate;

    @Override
    public Boolean isAllowed(Rule rule) {
        String key = "rate_limiter:fixed_window:" + rule.getLimitKey();
        RedisScript<Boolean> redisScript = LuaScriptSelector.getFixedWindowRateLimiterScript();

        // 构造参数列表，确保参数类型正确
        List<String> keys = Collections.singletonList(key);
        List<Object> args = Arrays.asList(rule.getLimitNum() + "", rule.getWindowSize() + "");

        // 执行Lua脚本
        return redisTemplate.execute(redisScript, keys, args.toArray());
    }


}