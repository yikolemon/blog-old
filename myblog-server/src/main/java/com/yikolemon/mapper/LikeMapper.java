package com.yikolemon.mapper;

import com.yikolemon.pojo.Like;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
@CacheConfig(cacheNames = "mapper:like")
public interface LikeMapper {

    int updateLikeOne(long blogId);

    @CacheEvict(key = "#blogId")
    int updateLike(long blogId, int num);

    @Cacheable(key = "#blogId")
    Like getLike(long blogId);

    //@CacheEvict(key = "#blogId")
    //其实是insert
    int setLike(long blogId);

    int deleteLike(long blogId);

}
