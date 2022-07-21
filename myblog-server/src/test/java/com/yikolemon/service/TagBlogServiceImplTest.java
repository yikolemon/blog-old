package com.yikolemon.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagBlogServiceImplTest {

    @Autowired
    private TagBlogServiceImpl tagBlogService;

    @Test
    public void saveTagBlogs() {
        int i = tagBlogService.saveTagBlogs("4,5,6", (long) 185);
        System.out.println(i);
    }

    @Test
    public void deleteTagByBlogId(){
        int i = tagBlogService.deleteTagByBlogId((long) 230);
        System.out.println(i);
    }
}