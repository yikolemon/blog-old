package com.yikolemon.cache.CaffeineRedis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;


@Configuration
@ConditionalOnProperty(prefix = "custom-manager",name="isOpen-custom-cache",havingValue = "true")
@EnableCaching
public class CaffeineRedisConfig{


    //选择使用redisCacheManager还是CaffeineCacheManager
    @Bean
    public CustomCaffeineRedisCacheResolver customCaffeineRedisCacheResolver(@Qualifier(value = "customCaffeineRedisManager") CustomCaffeineRedisManager cacheManager){
      System.out.println("打印manager");
        return new CustomCaffeineRedisCacheResolver(cacheManager);
    }

//  @Bean
//  public CustomCaffeineRedisCacheResolver customCaffeineRedisCacheResolver(@Qualifier(value = "redisCacheManager") RedisCacheManager cacheManager){
//    System.out.println("打印manager");
//    return new CustomCaffeineRedisCacheResolver(cacheManager);
//  }

    @Bean("customCaffeineRedisManager")
    public CustomCaffeineRedisManager customCaffeineRedisManager(   @Autowired(
        required = false
    )RedisCacheManager redisCacheManager,   @Autowired(
        required = false
    ) CaffeineCacheManager caffeineCacheManager){
      System.out.println("创建CaffeineRedismanager");
       return new CustomCaffeineRedisManager(caffeineCacheManager,redisCacheManager);
    }

    @Bean
    public CustomCachingConfigurer customCachingConfigurer(CustomCaffeineRedisCacheResolver customCaffeineRedisCacheResolver){
      CustomCachingConfigurer customCachingConfigurer =  new CustomCachingConfigurer();
      //设置默认的CacheManager
      customCachingConfigurer.setCacheManager(new CaffeineCacheManager());
      customCachingConfigurer.setCacheResolver(customCaffeineRedisCacheResolver);
      return customCachingConfigurer;
    }



}

