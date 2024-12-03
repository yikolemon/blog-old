package com.yikolemon.service;

import com.yikolemon.mapper.LikeMapper;
import com.yikolemon.pojo.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LikeServiceImpl implements LikeService {

    @Resource
    private LikeMapper likeMapper;

    private final ConcurrentHashMap<Long, Like> likeMap = new ConcurrentHashMap<>();

    @Override
    public Like getLike(long blogId) {
        likeMap.computeIfAbsent(blogId, key->{
            Like dbLike = likeMapper.getLike(blogId);
            if (dbLike == null){
                dbLike = new Like(0, blogId);
            }
            return dbLike;
        });
        return likeMap.get(blogId);
    }


    @Override
    //@CacheEvict(key = "#blogId")
    public int deleteLike(long blogId) {
        int res = likeMapper.deleteLike(blogId);
        likeMap.remove(blogId);
        return res;
    }

    @Override
    //给Controller使用的
    public int updateLikeOne(long blogId) {
        //更新
        likeMap.compute(blogId, (key, val) ->{
            if (val == null){
                Like dbLike = likeMapper.getLike(blogId);
                if (dbLike == null){
                    dbLike = new Like(0, blogId);
                }
                int like = dbLike.getLike();
                dbLike.setLike(like + 1);
                return dbLike;
            }else{
                int like = val.getLike();
                val.setLike(like + 1);
                return val;
            }
        });
        return 1;
    }

}
