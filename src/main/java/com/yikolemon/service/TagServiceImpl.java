package com.yikolemon.service;

import com.yikolemon.mapper.TagMapper;
import com.yikolemon.pojo.Tag;
import com.yikolemon.queue.IndexTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    @Transactional
    public int saveTag(Tag tag) {
        return tagMapper.saveTag(tag);
    }

    @Override
    @Transactional
    public Tag getTag(Long id) {
        return tagMapper.getTag(id);
    }

    @Override
    @Transactional
    public Tag getTagByName(String name) {
        return tagMapper.getTagByName(name);
    }

    @Override
    @Transactional
    public List<Tag> getAllTags() {
        return tagMapper.getAllTags();
    }

    @Override
    @Transactional
    public int updateTag(Tag tag) {
        return tagMapper.updateTag(tag);
    }

    @Override
    @Transactional
    public int deleteTag(Long id) {
        return tagMapper.deleteTag(id);
    }

    @Override
    public List<IndexTag> getTagTop(int size) {
        return tagMapper.getTagTop(size);
    }

    @Override
    public List<IndexTag> getAllIndexTags() {
        return tagMapper.getAllIndexTags();
    }

    @Override
    public List<Tag> getTagsByBlogId(long id) {
        return tagMapper.getTagsByBlogId(id);
    }
}
