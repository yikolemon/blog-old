package com.yikolemon.cache.CaffeineRedis;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;

public class CustomCachingConfigurer implements CachingConfigurer {

    private CacheManager cacheManager;

    private CacheResolver cacheResolver;

    private CacheErrorHandler cacheErrorHandler;

    private KeyGenerator keyGenerator;

    @Override
    public CacheManager cacheManager() {
        return this.cacheManager;
    }

    @Override
    public CacheResolver cacheResolver() {
        return this.cacheResolver;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return this.keyGenerator;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return this.cacheErrorHandler;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheResolver getCacheResolver() {
        return cacheResolver;
    }

    public void setCacheResolver(CacheResolver cacheResolver) {
        this.cacheResolver = cacheResolver;
    }

    public CacheErrorHandler getCacheErrorHandler() {
        return cacheErrorHandler;
    }

    public void setCacheErrorHandler(CacheErrorHandler cacheErrorHandler) {
        this.cacheErrorHandler = cacheErrorHandler;
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
}

