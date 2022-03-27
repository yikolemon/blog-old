package com.yikolemon.mapper;

import com.yikolemon.pojo.Blog;
import com.yikolemon.pojo.Like;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LikeMapper {

    int updateLikeOne(long blogId);

    int updateLike(long blogId,int num);

    Like getLike(long blogId);

    int setLike(long blogId);

    int deleteLike(long blogId);

}
