package com.yikolemon.shiro.shiroCache;

import com.yikolemon.util.ApplicationContextUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collection;
import java.util.Set;

//自定义redis缓存的实现
public class RedisShiroCache<k,v> implements Cache<k,v> {

    private String cacheName;

    //操作数据缓存的--跟着线程走的
    private  RedisTemplate redisTemplate;  //Redis的模板负责将缓存对象写到redis服务器里面去

    public RedisShiroCache() {
    }

    public RedisShiroCache(String cacheName){
        this.cacheName = cacheName;
    }


    @Override
    public v get(k k) throws CacheException {
        //System.out.println("get key:"+k);
        return (v) getRedisTemplate().opsForHash().get(this.cacheName,k.toString());
    }

    @Override
    public v put(k k, v v) throws CacheException {
        //System.out.println("put key: "+k);
        //System.out.println("put value:"+v);
        getRedisTemplate().opsForHash().put(this.cacheName,k.toString(),v);
        return null;
    }

    @Override
    public v remove(k k) throws CacheException {
        System.out.println("=============remove=============");
        return (v) getRedisTemplate().opsForHash().delete(this.cacheName,k.toString());
    }

    @Override
    public void clear() throws CacheException {
        System.out.println("=============clear==============");
        getRedisTemplate().delete(this.cacheName);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<k> keys() {
        return getRedisTemplate().opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<v> values() {
        return getRedisTemplate().opsForHash().values(this.cacheName);
    }

    private RedisTemplate getRedisTemplate(){
        if(redisTemplate == null){    //每个连接池的连接都要获得RedisTemplate
            redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        }
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        //redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
        //redisTemplate.setHashKeySerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }

}