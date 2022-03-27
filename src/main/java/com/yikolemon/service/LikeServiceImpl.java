package com.yikolemon.service;

import com.yikolemon.mapper.LikeMapper;
import com.yikolemon.pojo.Like;
import com.yikolemon.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Override
    public Like getLike(long blogId) {
        Jedis jedis = RedisUtil.getJedis();
        String s = jedis.hget("myblog-like", blogId + "");
        jedis.close();
        Like like = likeMapper.getLike(blogId);
        if (s==null) return like;
        else{
            like.setLike(like.getLike()+Integer.valueOf(s));
            return like;
        }
    }

    @Override
    public int setLike(long blogId) {
        return likeMapper.setLike(blogId);
    }

    @Override
    public int deleteLike(long blogId) {
        Jedis jedis = RedisUtil.getJedis();
        jedis.hdel("myblog-like", blogId + "");
        jedis.close();
        return likeMapper.deleteLike(blogId);
    }

    @Override
    //给定时器任务使用的update
    public int updateLike(long blogId, int num) {
        return likeMapper.updateLike(blogId, num);
    }

    @Override
    //给Controller使用的
    public int updateLikeOne(long blogId) {
        Jedis jedis = RedisUtil.getJedis();
        jedis.hincrBy("myblog-like", blogId + "",1);
        jedis.close();
        return 1;
    }

}
