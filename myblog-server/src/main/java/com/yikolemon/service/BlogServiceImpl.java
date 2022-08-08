package com.yikolemon.service;

import com.github.pagehelper.PageHelper;
import com.yikolemon.mapper.BlogMapper;
import com.yikolemon.pojo.Blog;
import com.yikolemon.queue.ArchiveBlog;
import com.yikolemon.queue.IndexBlog;
import com.yikolemon.queue.RightTopBlog;
import com.yikolemon.queue.SearchBlog;
import com.yikolemon.util.MarkdownUtils;
import com.yikolemon.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "blogs")
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private LikeService likeService;

    @Override
    @Cacheable(key = "'getBlog'+#id")
    public Blog getBlog(Long id) {
        return blogMapper.getBlog(id);
    }

    @Override
    @Cacheable(key = "'getAndConvert'+#id")
    public Blog getAndConvert(Long id) {
        PageHelper.clearPage();
        Blog blog = blogMapper.getBlog(id);
        String content = blog.getContent();
        String str = MarkdownUtils.markdownToHtmlExtensions(content);
        blog.setContent(str);
        return blog;
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public int saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setView(0);
        blogMapper.saveBlog(blog);
        return  likeService.setLike(blog.getId());

    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateBlog(Blog blog) {
        blog.setUpdateTime(new Date());
        return blogMapper.updateBlog(blog);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public int deleteBlog(Long id) {
        likeService.deleteLike(id);
        return blogMapper.deleteBlog(id);
    }


    @Override
    //@Cacheable(key = "'listBlogsIndex'")
    public List<IndexBlog> listBlogsIndex() {
        return blogMapper.listBlogsIndex();
    }

    @Override
    public List<IndexBlog> searchBlog(String title) {
        return blogMapper.searchBlog(title);
    }

    @Override
    //@Cacheable(key = "'listRecommendNewBlog'+#size")
    public List<RightTopBlog> listRecommendNewBlog(int size) {
        return blogMapper.listRecommendNewBlog(size);
    }

    @Override
    //@Cacheable(key = "'listMostviewBlog'+#size")
    public List<RightTopBlog> listMostviewBlog(int size) {
        return blogMapper.listMostviewBlog(size);
    }

    @Override
    //@Cacheable(key = "'listBlogsAdmin'")
    public List<Blog> listBlogsAdmin() {
        return blogMapper.listBlogsAdmin();
    }

    @Override
    //@Cacheable(key ="listAllBlogsSearch+#blog")
    public List<Blog> listAllBlogsSearch(SearchBlog blog) {
        return blogMapper.listAllBlogsSearch(blog);
    }

    @Override
    @Cacheable(key = "'listBlogsByTypeId'+#id")
    public List<IndexBlog> listBlogsByTypeId(Long id) {
        return blogMapper.listBlogsByTypeId(id);
    }

    @Override
    @Cacheable(key = "'listBlogsByTagId'+#id")
    public List<IndexBlog> listBlogsByTagId(Long id) {
        return blogMapper.listBlogsByTagId(id);
    }

    @Override
    @Cacheable(key = "'listBlogsArchive'")
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
    @Cacheable(key = "'countBlog'")
    public int countBlog() {
        return blogMapper.countBlog();
    }

    @Override
    //给redis定时任务使用
    @CacheEvict(allEntries = true)
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
