package com.yikolemon.service;

import com.yikolemon.pojo.Like;

public interface LikeService {

    int updateLikeOne(long blogId);

    Like getLike(long blogId);

    int deleteLike(long blogId);

}
