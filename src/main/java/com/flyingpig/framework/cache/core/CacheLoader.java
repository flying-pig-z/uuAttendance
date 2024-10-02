package com.flyingpig.framework.cache.core;

/**
 * 缓存加载器
 * 用于将数据库信息加载到缓存中
 */
@FunctionalInterface
public interface CacheLoader<T> {

    /**
     * 加载缓存
     */
    T load();
}