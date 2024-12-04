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


    @CacheEvict(key = "#blogId")
    int updateLike(long blogId, long num);

    @Cacheable(key = "#blogId")
    Like getLike(long blogId);

    int deleteLike(long blogId);

}
