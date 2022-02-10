package com.yikolemon.mapper;

import com.yikolemon.queue.ArchiveBlog;
import com.yikolemon.service.BlogServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogMapperTest {

    @Autowired
    private BlogMapper blogMapper;

    @Test
    public void getAllYears() {
        String[] allYears = blogMapper.getAllYears();
        for (String str:
             allYears) {
            System.out.println(str);
        }
    }

    @Test
    public void getBlogsByYear() {
        List<ArchiveBlog> blogsByYear = blogMapper.getBlogsByYear("2022");
        System.out.println(blogsByYear);
    }

    @Test
    public void countBlog() {
        System.out.println(blogMapper.countBlog());
    }
}