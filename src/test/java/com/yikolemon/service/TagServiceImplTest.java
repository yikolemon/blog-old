package com.yikolemon.service;

import com.yikolemon.pojo.Tag;
import com.yikolemon.queue.IndexTag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest

public class TagServiceImplTest {

    @Autowired
    private TagServiceImpl tagService;

    @Test
    public void saveTag() {
        Tag tag=new Tag();
        tag.setName("标签2");
        tagService.saveTag(tag);
    }

    @Test
    public void getTag() {
        System.out.println(tagService.getTag((long)1));
    }

    @Test
    public void getTagByName() {
        System.out.println(tagService.getTagByName("标签1"));
    }

    @Test
    public void getAllTags() {
        List<Tag> allTags = tagService.getAllTags();
        for (Tag tag:
             allTags) {
            System.out.println(tag);
        }
    }

    @Test
    public void updateTag() {
        Tag tag=new Tag();
        tag.setId((long)1);
        tag.setName("标签3");
        tagService.updateTag(tag);
    }

    @Test
    public void deleteTag() {
        int i = tagService.deleteTag((long) 2);
        System.out.println(i);
    }

    @Test
    public void getTagTop(){
        List<IndexTag> tagTop = tagService.getTagTop(2);
        System.out.println(tagTop);
    }

    @Test
    public void getAllIndexTags(){
        List<IndexTag> allIndexTags = tagService.getAllIndexTags();
        System.out.println(allIndexTags);
    }


}