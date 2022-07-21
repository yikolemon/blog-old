package com.yikolemon.service;

import com.yikolemon.pojo.Like;

public interface LikeService {

    int updateLikeOne(long blogId);

    int updateLike(long blogId, int num);

    Like getLike(long blogId);

    int setLike(long blogId);

    int deleteLike(long blogId);

}
