/*
package com.yikolemon.cache;

import com.yikolemon.util.ApplicationContextUtils;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

*/
/*
    使用redis实现mybatis二级缓存
 *//*

public class MyRedisCache implements Cache {

    //缓存对象唯一标识
    private final String id; //orm的框架都是按对象的方式缓存，而每个对象都需要一个唯一标识.
    //用于事务性缓存操作的读写锁
    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock(); //处理事务性缓存中做的
    //操作数据缓存的--跟着线程走的
    private  RedisTemplate redisTemplate;  //Redis的模板负责将缓存对象写到redis服务器里面去
    //缓存对象的是失效时间，30分钟
    private static final long EXPRIRE_TIME_IN_MINUT = 30;

    //构造方法---把对象唯一标识传进来
    public MyRedisCache(String id){
        if(id == null){
            throw new IllegalArgumentException("缓存对象id是不能为空的");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }


    //给模板对象RedisTemplate赋值，并传出去
    private RedisTemplate getRedisTemplate(){
        if(redisTemplate == null){    //每个连接池的连接都要获得RedisTemplate
            redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        }
        redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashKeySerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }


    */
/*
        保存缓存对象的方法
     *//*

    @Override
    public void putObject(Object key, Object value) {
        try{
            RedisTemplate redisTemplate = getRedisTemplate();
            //使用redisTemplate得到值操作对象
            ValueOperations operation = redisTemplate.opsForValue();
            //使用值操作对象operation设置缓存对象
            operation.set(key,value,EXPRIRE_TIME_IN_MINUT, TimeUnit.MINUTES);  //TimeUnit.MINUTES系统当前时间的分钟数
            //System.out.println("缓存对象保存成功");
        }catch (Throwable t){
            //System.out.println("缓存对象保存失败"+t);
        }

    }

    */
/*
        获取缓存对象的方法
     *//*

    @Override
    public Object getObject(Object key) {
        try {
            RedisTemplate redisTemplate = getRedisTemplate();
            ValueOperations operations = redisTemplate.opsForValue();
            Object result = operations.get(key);
            //System.out.println("获取缓存对象");
            return result;
        }catch (Throwable t){
            //System.out.println("缓存对象获取失败"+t);
            return null;
        }
    }

    */
/*
        删除缓存对象
     *//*

    @Override
    public Object removeObject(Object key) {
        try{
            RedisTemplate redisTemplate = getRedisTemplate();
            redisTemplate.delete(key);
            //System.out.println("删除缓存对象成功");
        }catch (Throwable t){
            //System.out.println("删除缓存对象失败");
        }
        return null;
    }

    */
/*
        清空缓存对象
        当缓存的对象更新了的化，就执行此方法
     *//*

    @Override
    public void clear() {
        RedisTemplate redisTemplate = getRedisTemplate();
        //回调函数
        redisTemplate.execute((RedisCallback)collection->{
            collection.flushDb();
            return  null;
        });
        //System.out.println("清空缓存对象成功");
    }

    //可选实现的方法
    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }



}*/
