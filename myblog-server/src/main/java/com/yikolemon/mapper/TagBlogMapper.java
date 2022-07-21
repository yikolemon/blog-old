package com.yikolemon.mapper;

import com.yikolemon.pojo.Blog;
import com.yikolemon.pojo.Tag;
import com.yikolemon.pojo.TagBlog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TagBlogMapper {

    int saveTagAndBlog(TagBlog tagBlog);

    Blog[] queueByTagId(long id);

    Tag[] queueByBlogId(long id);

    int deleteTagByBlogId(long id);
}
