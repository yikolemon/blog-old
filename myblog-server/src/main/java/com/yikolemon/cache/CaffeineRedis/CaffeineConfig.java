package com.yikolemon.cache.CaffeineRedis;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnProperty(prefix = "custom-manager",name="isOpen-custom-cache",havingValue = "true")
@ConditionalOnExpression(value = "'${custom-manager.cache-type}'== 'caffeine'|| '${custom-manager.cache-type}'=='caffeine-redis'")
public class CaffeineConfig {
    @Bean
    public CaffeineCacheManager caffeineCacheManager(){
        System.out.println("caffeineCacheManager被创建");
        CaffeineCacheManager caffeineManager = new CaffeineCacheManager();
        caffeineManager.setCaffeine(Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterAccess(200, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000));
        return caffeineManager;
    }
}
