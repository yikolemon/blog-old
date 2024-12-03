package com.yikolemon.service;

import com.yikolemon.mapper.TagMapper;
import com.yikolemon.pojo.Tag;
import com.yikolemon.queue.IndexTag;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames = "tags")
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public int saveTag(Tag tag) {
        return tagMapper.saveTag(tag);
    }

    @Override
    @Transactional
    @Cacheable(key = "'getTag'+#id")
    public Tag getTag(Long id) {
        return tagMapper.getTag(id);
    }

    @Override
    @Transactional
    @Cacheable(key = "'getTagByName'+#name")
    public Tag getTagByName(String name) {
        return tagMapper.getTagByName(name);
    }

    @Override
    @Transactional
    @Cacheable(key = "'getAllTags'")
    public List<Tag> getAllTags() {
        return tagMapper.getAllTags();
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public int updateTag(Tag tag) {
        return tagMapper.updateTag(tag);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public int deleteTag(Long id) {
        return tagMapper.deleteTag(id);
    }

    @Override
    @Cacheable(key = "'getTagTop'+#size")
    public List<IndexTag> getTagTop(int size) {
        return tagMapper.getTagTop(size);
    }

    @Override
    @Cacheable(key = "'getAllIndexTags'")
    public List<IndexTag> getAllIndexTags() {
        return tagMapper.getAllIndexTags();
    }

    @Override
    @Cacheable(key = "'getTagsByBlogId'+#id")
    public List<Tag> getTagsByBlogId(long id) {
        return tagMapper.getTagsByBlogId(id);
    }
}
