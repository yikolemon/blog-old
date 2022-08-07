package com.yikolemon.util;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

import static org.junit.Assert.*;

public class RedisUtilTest {

    @Test
    public void getJedis() {
        Jedis jedis = RedisUtil.getJedis();
        //jedis.hset("hi","1223","23");
        Map<String, String> hi = jedis.hgetAll("hi");
        System.out.println(hi);
        System.out.println(jedis);
    }

    @Test
    public void release() {
    }
}