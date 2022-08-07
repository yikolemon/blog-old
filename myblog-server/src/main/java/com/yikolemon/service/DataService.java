package com.yikolemon.service;

import com.yikolemon.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class DataService {

    @Autowired
    private RedisTemplate redisTemplate;

    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    //将指定ip计入uv
    public void recordUV(String ip) {
        String redisKey = RedisKeyUtil.getUVKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey, ip);
    }

    //统计指定日期范围内的UV,统计范围内
    public long calculateUv(Date start,Date end){
        if (start==null||end==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        List<String> keyList=new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        instance.setTime(start);
        while (!instance.getTime().after(end)){
            String key = RedisKeyUtil.getUVKey(df.format(instance.getTime()));
            keyList.add(key);
            instance.add(Calendar.DATE,1);
        }
        //合并数据
        String redisKey = RedisKeyUtil.getUVKey(df.format(start), df.format(end));
        redisTemplate.opsForHyperLogLog().union(redisKey,keyList.toArray());
        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }

    //指定用户计入Dau
    public void recordDAU(int userId){
        String redisKey = RedisKeyUtil.getDAUKey(df.format(new Date()));
        redisTemplate.opsForValue().setBit(redisKey,userId,true);
    }

    //统计指定日期范围内的DAU
    public long calculateDAU(Date start,Date end){
        if (start==null||end==null){
            throw new IllegalArgumentException("参数不能为空");
        }

        List<byte[]> keyList=new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        instance.setTime(start);
        while (!instance.getTime().after(end)){
            String key = RedisKeyUtil.getUVKey(df.format(instance.getTime()));
            keyList.add(key.getBytes());
            instance.add(Calendar.DATE,1);
        }
        //进行or运算
        return (long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                String redisKey = RedisKeyUtil.getDAUKey(df.format(start), df.format(end));
                connection.bitOp(RedisStringCommands.BitOperation.OR,redisKey.getBytes(),keyList.toArray(new byte[0][0]));
                return connection.bitCount(redisKey.getBytes());
            }
        });
    }
}