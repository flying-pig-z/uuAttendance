package com.flyingpig.framework.ratelimiter.core.strategy;

import com.flyingpig.framework.ratelimiter.model.Rule;
import org.springframework.stereotype.Component;

/*
    限流策略
 */
public interface RateLimitStrategy {
    Boolean isAllowed(Rule rule);
}
