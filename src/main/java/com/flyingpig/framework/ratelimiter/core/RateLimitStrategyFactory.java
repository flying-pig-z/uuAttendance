package com.flyingpig.framework.ratelimiter.core;

import com.flyingpig.framework.ratelimiter.core.strategy.*;
import com.flyingpig.framework.ratelimiter.model.Mode;
import com.flyingpig.framework.ratelimiter.model.Rule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

import static com.baomidou.mybatisplus.core.toolkit.CollectionUtils.newHashMap;
@Component
@RequiredArgsConstructor
public class RateLimitStrategyFactory {
    private static final Map<Mode, RateLimitStrategy> rateLimitStrategies = newHashMap();// 策略存储容器

    private final SlideWindowLimitStrategy slideWindowLimitStrategy;
    private final FixedWindowLimitStrategy fixedWindowLimitStrategy;
    private final TokenBucketLimitStrategy tokenBucketLimitStrategy;


    @PostConstruct
    public void init() {
        rateLimitStrategies.put(Mode.SLIDE_WINDOW, slideWindowLimitStrategy);
        rateLimitStrategies.put(Mode.FIXED_WINDOW, fixedWindowLimitStrategy);
        rateLimitStrategies.put(Mode.TOKEN_BUCKET, tokenBucketLimitStrategy);
    }

    public boolean isAllowed(Rule rule) {
        return rateLimitStrategies.get(rule.getMode()).isAllowed(rule);
    }

}
