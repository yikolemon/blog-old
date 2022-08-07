package com.yikolemon.cache.CaffeineRedis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.time.Duration;

@Configuration
@ConditionalOnProperty(prefix = "custom-manager",name="isOpen-custom-cache",havingValue = "true")
@ConditionalOnExpression(value = "'${custom-manager.cache-type}'== 'redis'|| '${custom-manager.cache-type}'=='caffeine-redis'")
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig{
    static{
        System.out.println("RedisConfig被创建");
    }

    //1000秒
    public static final long ttl=1000;

    @Bean
    public CustomeRedisCacheWriter customeRedisCacheWriter(RedisConnectionFactory connectionFactory){
        return new CustomeRedisCacheWriter(connectionFactory);
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        return setSerializer(redisCacheConfiguration);
    }
    @Bean
    public RedisCacheManager redisCacheManager(CustomeRedisCacheWriter customeRedisCacheWriter, RedisCacheConfiguration redisCacheConfiguration) {
        System.out.println("redisCacheManager被创建");
        redisCacheConfiguration = redisCacheConfiguration
                .entryTtl(Duration.ofSeconds(ttl))//有效期
                .disableCachingNullValues();//不缓存空值
        RedisCacheManager redisCacheManager = new RedisCacheManager(customeRedisCacheWriter,redisCacheConfiguration);
        return  redisCacheManager;
    }

    private RedisCacheConfiguration setSerializer(RedisCacheConfiguration redisCacheConfiguration) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        StringRedisSerializer stringRedisSerializer =new StringRedisSerializer();
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisCacheConfiguration = redisCacheConfiguration.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer));
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
        return redisCacheConfiguration;
    }


}

