package com.yikolemon.cache.CaffeineRedis;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.cache.Cache;

public class CustomCaffeineRedisCacheContainer {

    private  volatile static Map<String, Cache> cacheContainer=new ConcurrentHashMap<>();

    public Map<String, Cache> getCacheContainer() {
        return cacheContainer;
    }
}

