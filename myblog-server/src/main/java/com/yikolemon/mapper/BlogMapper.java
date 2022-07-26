package com.yikolemon.mapper;


import com.yikolemon.pojo.Blog;
import com.yikolemon.queue.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BlogMapper {

    int saveBlog(Blog blog);

    Blog getBlog(Long id);

    int updateBlog(Blog blog);

    int deleteBlog(Long id);

    List<IndexBlog> listBlogsIndex();

    List<IndexBlog> searchBlog(String title);

    List<RightTopBlog> listRecommendNewBlog(int size);

    List<RightTopBlog> listMostviewBlog(int size);

    List<Blog> listBlogsAdmin();

    List<Blog> listAllBlogsSearch(SearchBlog blog);

    List<IndexBlog> listBlogsByTypeId(Long id);

    List<IndexBlog> listBlogsByTagId(Long id);

    String[] getAllYears();

    List<ArchiveBlog> getBlogsByYear(String year);

    int countBlog();

    int updateView(Long id, int num);


}
