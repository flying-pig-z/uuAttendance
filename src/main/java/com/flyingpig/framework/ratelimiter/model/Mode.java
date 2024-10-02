package com.flyingpig.framework.ratelimiter.model;

public enum Mode {
    /**
     * 时间窗口模式
     */
    FIXED_WINDOW,

    /**
     * 滑动窗口模式
     */
    SLIDE_WINDOW,
    /**
     * 令牌桶模式
     */
    TOKEN_BUCKET

}