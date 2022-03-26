package com.yikolemon.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yikolemon.pojo.Blog;
import com.yikolemon.queue.ArchiveBlog;
import com.yikolemon.queue.IndexBlog;
import com.yikolemon.queue.RightTopBlog;
import com.yikolemon.queue.SearchBlog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest

public class BlogServiceImplTest {

    @Autowired
    private BlogServiceImpl blogService;

    @Test
    public void getBlog() {
        System.out.println(blogService.getBlog((long)185));
    }

    @Test
    public void saveBlog() {
        for (int i = 11; i <20; i++) {
            Blog blog=new Blog();
            blog.setTitle("blog"+i);
            blog.setPublished(true);
            blog.setCommentabled(false);
            blog.setShareStatement(false);
            blog.setRecommend(false);
            blog.setTypeId((long)1);
            System.out.println(blogService.saveBlog(blog));

        }

    }

    @Test
    public void listBlogsIndex() {
        List<IndexBlog> indexBlogs = blogService.listBlogsIndex();
        for (IndexBlog blog:
             indexBlogs) {
            System.out.println(blog);
        }
    }

    @Test
    public void updateBlog() {
        Blog blog=new Blog();
        blog.setTitle("blog");
        blog.setId((long)185);
        blog.setCommentabled(false);
        blog.setShareStatement(false);
        blog.setRecommend(false);
        blog.setTypeId((long)1);
        int i = blogService.updateBlog(blog);
        System.out.println(i);

    }

    @Test
    public void deleteBlog() {
        blogService.deleteBlog((long)3);
    }


    @Test
    public void listBlogsAdmin(){
        List<Blog> list = blogService.listBlogsAdmin();
        for (Blog blog:
                list) {
            System.out.println(blog);
        }
    }

    @Test
    public void listAllBlogsSearch(){
        SearchBlog blog=new SearchBlog();
        blog.setRecommend(false);
        blog.setTitle("2");
        blog.setTypeId((long)1);
        List<Blog> list = blogService.listAllBlogsSearch(blog);
        for (Blog blog1:
             list) {
            System.out.println(blog1);
        }
    }

    @Test
    public void listRecommendNewBlog(){
        List<RightTopBlog> rightTopBlogs = blogService.listRecommendNewBlog(6);
        System.out.println(rightTopBlogs);
    }

    @Test
    public void listMostViewBlog(){
        List<RightTopBlog> rightTopBlogs = blogService.listMostviewBlog(6);
        System.out.println(rightTopBlogs);
    }

    @Test
    public void searchBlog(){
        List<IndexBlog> indexBlogs = blogService.searchBlog("1");
        System.out.println(indexBlogs);
    }


    @Test
    public void listBlogsArchive() {
        Map<String, List<ArchiveBlog>> stringListMap = blogService.listBlogsArchive();
        System.out.println(stringListMap);
    }

    @Test
    public void updateLike() {
        int i = blogService.updateLike((long) 251);
        System.out.println(i);

    }

    @Test
    public void getLike() {
        Blog like = blogService.getLike((long) 251);
        System.out.println(like);
    }
}