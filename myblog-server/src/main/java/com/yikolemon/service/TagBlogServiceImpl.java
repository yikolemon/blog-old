package com.yikolemon.service;

import com.yikolemon.mapper.TagBlogMapper;
import com.yikolemon.pojo.Tag;
import com.yikolemon.pojo.TagBlog;
import com.yikolemon.util.TagBlogUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@CacheConfig(cacheNames = "tags")
public class TagBlogServiceImpl implements TagBlogService{

    @Resource
    private TagBlogMapper tagBlogMapper;

    @Override
    @Transactional
    public int saveTagBlogs(String tagIdStr,long blogId) {
        if (tagIdStr==null||tagIdStr.isEmpty()) {
            return 0;
        }
        long[] tagIds = TagBlogUtils.tagIds(tagIdStr);
        int num=0;
        for (int i = 0; i <tagIds.length; i++) {
            num+=tagBlogMapper.saveTagAndBlog(new TagBlog(tagIds[i],blogId));
        }
        return num;
    }


    @Override
    public String StringTagIdsByBlogId(long id) {
        Tag[] tags = tagBlogMapper.queueByBlogId(id);
        if (tags==null) {
            return "";
        }
        long[] arr=new long[tags.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i]=tags[i].getId();
        }
        return TagBlogUtils.tagIds(arr);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteTagByBlogId(long id) {
        return tagBlogMapper.deleteTagByBlogId(id);
    }

}
