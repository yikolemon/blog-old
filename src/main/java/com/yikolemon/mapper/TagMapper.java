package com.yikolemon.mapper;

import com.yikolemon.pojo.Tag;
import com.yikolemon.queue.IndexTag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagMapper {

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
