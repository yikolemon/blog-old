package com.yikolemon.cache.CaffeineRedis;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleCacheResolver;

import java.util.Collection;

public class CustomCaffeineRedisCacheResolver extends SimpleCacheResolver implements CacheResolver  {
    public CustomCaffeineRedisCacheResolver(CacheManager cacheManager) {
        super(cacheManager);
    }
}

