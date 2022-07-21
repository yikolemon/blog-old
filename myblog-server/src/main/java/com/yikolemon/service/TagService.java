package com.yikolemon.service;

import com.yikolemon.pojo.Tag;
import com.yikolemon.queue.IndexTag;

import java.util.List;

public interface TagService {
    int saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    List<Tag> getAllTags();

    int updateTag(Tag tag);

    int deleteTag(Long id);

    List<IndexTag> getTagTop(int size);

    List<IndexTag> getAllIndexTags();

    List<Tag> getTagsByBlogId(long id);
}
