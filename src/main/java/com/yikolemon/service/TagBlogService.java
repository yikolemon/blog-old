package com.yikolemon.service;

import com.yikolemon.pojo.Tag;
import com.yikolemon.pojo.TagBlog;

public interface TagBlogService {

    int saveTagBlogs(String tagIds,long id);

    String StringTagIdsByBlogId(long id);

    int deleteTagByBlogId(long id);

}
