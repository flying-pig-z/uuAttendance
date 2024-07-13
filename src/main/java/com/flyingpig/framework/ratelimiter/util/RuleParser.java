package com.flyingpig.framework.ratelimiter.util;

import com.flyingpig.framework.ratelimiter.annotation.RateLimit;
import com.flyingpig.framework.ratelimiter.model.Rule;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.stereotype.Component;


@Component
public class RuleParser {
    private final TokenParser tokenParser = new TokenParser();


    public Rule getRateLimiterRule(JoinPoint joinPoint, RateLimit rateLimit) {
        Rule rule = new Rule();
        // 设置限流模式
        rule.setMode(rateLimit.mode());
        // 设置限流key
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String rateLimitKey = tokenParser.getKey(signature) + tokenParser.getParamsKey(joinPoint, rateLimit);
        rule.setLimitKey(rateLimitKey);
        // 设置固定窗口/滑动窗口算法参数
        rule.setWindowSize((int) DurationStyle.detectAndParse(rateLimit.windowSize()).getSeconds());
        rule.setLimitNum(rateLimit.limitNum());
        // 设置令牌桶算法参数
        rule.setConsumeTokenPerReq(rateLimit.consumeTokenPerReq());
        rule.setBucketCapacity(rateLimit.bucketCapacity());
        rule.setGenerateTokenRate(rateLimit.generateTokenRate());
        return rule;
    }

}