package com.yikolemon.service;

import com.yikolemon.mapper.BlogMapper;
import com.yikolemon.mapper.LikeMapper;
import com.yikolemon.pojo.Blog;
import com.yikolemon.queue.ArchiveBlog;
import com.yikolemon.queue.IndexBlog;
import com.yikolemon.queue.RightTopBlog;
import com.yikolemon.queue.SearchBlog;
import com.yikolemon.util.MarkdownUtils;
import com.yikolemon.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private LikeService likeService;

    @Override
    public Blog getBlog(Long id) {
        return blogMapper.getBlog(id);
    }

    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogMapper.getBlog(id);
        String content = blog.getContent();
        String str = MarkdownUtils.markdownToHtmlExtensions(content);
        blog.setContent(str);
        return blog;
    }

    @Override
    @Transactional
    public int saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setView(0);
        blogMapper.saveBlog(blog);
        return  likeService.setLike(blog.getId());

    }

    @Override
    public int updateBlog(Blog blog) {
        blog.setUpdateTime(new Date());
        return blogMapper.updateBlog(blog);
    }

    @Override
    @Transactional
    public int deleteBlog(Long id) {
        likeService.deleteLike(id);
        return blogMapper.deleteBlog(id);
    }


    @Override
    public List<IndexBlog> listBlogsIndex() {
        return blogMapper.listBlogsIndex();
    }

    @Override
    public List<IndexBlog> searchBlog(String title) {
        return blogMapper.searchBlog(title);
    }

    @Override
    public List<RightTopBlog> listRecommendNewBlog(int size) {
        return blogMapper.listRecommendNewBlog(size);
    }

    @Override
    public List<RightTopBlog> listMostviewBlog(int size) {
        return blogMapper.listMostviewBlog(size);
    }

    @Override
    public List<Blog> listBlogsAdmin() {
        return blogMapper.listBlogsAdmin();
    }

    @Override
    public List<Blog> listAllBlogsSearch(SearchBlog blog) {
        return blogMapper.listAllBlogsSearch(blog);
    }

    @Override
    public List<IndexBlog> listBlogsByTypeId(Long id) {
        return blogMapper.listBlogsByTypeId(id);
    }

    @Override
    public List<IndexBlog> listBlogsByTagId(Long id) {
        return blogMapper.listBlogsByTagId(id);
    }

    @Override
    public Map<String, List<ArchiveBlog>> listBlogsArchive() {
        String[] years = blogMapper.getAllYears();
        Map map=new HashMap<String,List<ArchiveBlog>>();
        for (int i = 0; i < years.length; i++) {
            List<ArchiveBlog> blogsByYear = blogMapper.getBlogsByYear(years[i]);
            map.put(years[i],blogsByYear);
        }
        return map;
    }

    @Override
    public int countBlog() {
        return blogMapper.countBlog();
    }

    @Override
    //给redis定时任务使用
    public int updateView(Long id,int num) {
        return blogMapper.updateView(id,num);
    }

    @Override
    public int updateViewOne(Long id) {
        Jedis jedis = RedisUtil.getJedis();
        jedis.hincrBy("myblog-view", id + "",1);
        jedis.close();
        return 1;
    }

}
