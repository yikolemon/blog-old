package com.yikolemon.mapper;

import com.yikolemon.pojo.Blog;
import com.yikolemon.queue.ArchiveBlog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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

    @Test
    public void saveBlog() {
        Blog blog = new Blog();
        blog.setView(0);
        blog.setShareStatement(false);
        blog.setCommentabled(false);
        blog.setRecommend(false);
        blog.setPublished(false);
        blog.setTypeId((long)26);
        int i = blogMapper.saveBlog(blog);
        System.out.println(blog.getId());
        System.out.println(i);
    }
}