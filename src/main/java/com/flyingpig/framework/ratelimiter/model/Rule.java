package com.flyingpig.framework.ratelimiter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rule {

    // 限流模式
    private Mode mode;

    // 限制的key
    private String limitKey;

    // 窗口中限制的请求数量
    private int limitNum;

    // 窗口大小
    private int windowSize;

    //令牌桶容量
    private int bucketCapacity;

    // 每秒生成的令牌数量
    private int generateTokenRate;

    // 每次请求消耗的令牌数量
    private int consumeTokenPerReq;

    public Rule(String rateLimitKey, int rate, Mode mode) {
        this.limitKey = rateLimitKey;
        this.limitNum = rate;
        this.mode = mode;
    }
}
