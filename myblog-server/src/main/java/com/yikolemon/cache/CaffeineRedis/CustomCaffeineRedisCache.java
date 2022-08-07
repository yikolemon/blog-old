package com.yikolemon.cache.CaffeineRedis;

import org.apache.ibatis.cache.CacheException;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.Callable;

public class CustomCaffeineRedisCache implements Cache {
    private final String name;
    private final static Object lock = new Object();
    private final Cache caffeineCache;
    private final Cache redisCache;
    private volatile Object  value;

    public CustomCaffeineRedisCache(String name,Cache caffeineCache,Cache redisCache){
        this.name = name;
        this.caffeineCache = caffeineCache;
        this.redisCache = redisCache;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public ValueWrapper get(Object key) {
        if(!ObjectUtils.isEmpty(caffeineCache)&&!ObjectUtils.isEmpty(redisCache)){
            //一级缓存获取值
            value = caffeineCache.get(key);
            //如果一级缓存没有该值，降低redis访问压力
            if(ObjectUtils.isEmpty(value)){
                synchronized (lock){
                    value = caffeineCache.get(key);
                    if(ObjectUtils.isEmpty(value)){
                        value = redisCache.get(key);
                        if (value!=null){//如果二级缓存有,就存到caffeineCache中
                            caffeineCache.put(key, ((ValueWrapper)(this.value)).get());
                        }
                    }
                }
            }
        }else if(!ObjectUtils.isEmpty(caffeineCache)){
                value = caffeineCache.get(key);

        }else if(!ObjectUtils.isEmpty(redisCache)){
            value = redisCache.get(key);
        }else{
            throw new CacheException("Cache is  null");
        }
        return (ValueWrapper) value;
    }

    @Override
    public <T> T get(Object key, Class<T> aClass) {
        if(!ObjectUtils.isEmpty(caffeineCache)&&!ObjectUtils.isEmpty(redisCache)){
            //一级缓存获取值
            value = caffeineCache.get(key,aClass);
            //如果一级缓存没有该值，降低redis访问压力
            if(ObjectUtils.isEmpty(value)){
                synchronized (lock){
                    value = caffeineCache.get(key);
                    if(ObjectUtils.isEmpty(value)){
                        value = redisCache.get(key,aClass);
                        caffeineCache.put(key,value);
                    }
                }
            }
        }else{
            if(!ObjectUtils.isEmpty(caffeineCache)){
                value = caffeineCache.get(key);
            }else{
                value = redisCache.get(key);
            }
        }
        return (T)value;
    }

    @Override
    public synchronized  <T> T get(Object key, Callable<T> valueLoader) {
        if(!ObjectUtils.isEmpty(caffeineCache)&&!ObjectUtils.isEmpty(redisCache)){
            //一级缓存获取值
            value = caffeineCache.get(key);
            //如果一级缓存没有该值，降低redis访问压力
            if(ObjectUtils.isEmpty(value)){
                value = redisCache.get(key,valueLoader);
                redisCache.put(key,value);
                caffeineCache.put(key,value);
                return (T) value;
            }else{
                return (T) ((ValueWrapper)value).get();
            }
        }else{
            if(!ObjectUtils.isEmpty(caffeineCache)){
                value = caffeineCache.get(key,valueLoader);
                caffeineCache.put(key,value);
                return (T) value;
            }else{
                value = redisCache.get(key,valueLoader);
                redisCache.put(key,value);
                return (T)value;
            }
        }
    }

    @Override
    public void put(Object key, Object value) {
        if(!ObjectUtils.isEmpty(caffeineCache)&&!ObjectUtils.isEmpty(redisCache)){
            caffeineCache.put(key,value);
            redisCache.put(key,value);
        }else if(!ObjectUtils.isEmpty(caffeineCache)){
            caffeineCache.put(key,value);
        }else if(!ObjectUtils.isEmpty(redisCache)){
            redisCache.put(key,value);
        }

    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        caffeineCache.put(key,value);
        redisCache.put(key,value);
        return new SimpleValueWrapper(value);
    }

    @Override
    public void evict(Object key) {
        if(!ObjectUtils.isEmpty(caffeineCache)&&!ObjectUtils.isEmpty(redisCache)){
            //先删除二级缓存
            redisCache.evict(key);
            caffeineCache.evict(key);
        }else if(!ObjectUtils.isEmpty(caffeineCache)){
            caffeineCache.evict(key);
        }else if(!ObjectUtils.isEmpty(redisCache)){
            redisCache.evict(key);
        }
    }

    @Override
    public boolean evictIfPresent(Object key) {
        if(!ObjectUtils.isEmpty(caffeineCache)&&!ObjectUtils.isEmpty(redisCache)){
            redisCache.evictIfPresent(key);
            return caffeineCache.evictIfPresent(key);
        }else if(!ObjectUtils.isEmpty(caffeineCache)){
            return caffeineCache.evictIfPresent(key);
        }else if(!ObjectUtils.isEmpty(redisCache)){
            return redisCache.evictIfPresent(key);
        }else {
           throw new CacheException("无可用缓存");
        }
    }

    @Override
    public void clear() {
        if(!ObjectUtils.isEmpty(caffeineCache)&&!ObjectUtils.isEmpty(redisCache)){
            redisCache.clear();
            caffeineCache.clear();
        }else if(!ObjectUtils.isEmpty(caffeineCache)){
            caffeineCache.clear();
        }else if(!ObjectUtils.isEmpty(redisCache)){
            redisCache.clear();
        }
    }

    @Override
    public boolean invalidate() {
        if(!ObjectUtils.isEmpty(caffeineCache)&&!ObjectUtils.isEmpty(redisCache)){
            redisCache.invalidate();
            return  caffeineCache.invalidate();
        }else if(!ObjectUtils.isEmpty(caffeineCache)){
            return  caffeineCache.invalidate();
        }else if(!ObjectUtils.isEmpty(redisCache)){
            return redisCache.invalidate();
        }

        return false;
    }
}

