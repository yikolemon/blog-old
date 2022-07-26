package com.yikolemon.service;

import com.yikolemon.mapper.elasticsearch.BlogRepository;
import com.yikolemon.pojo.Blog;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchService {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private ElasticsearchRestTemplate elasticTemplate;


    public void saveBlog(Blog blog){
        blogRepository.save(blog);
    }

    public void deleteBlog(int id){
        blogRepository.deleteById(id);
    }

    public Page<Blog> searchBlogs(String keyword,int pageNum){
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content", "description"))
                //推荐放在前面
                .withSort(SortBuilders.fieldSort("recommend").order(SortOrder.DESC))
                //时间倒序
                .withSort(SortBuilders.fieldSort("view").order(SortOrder.DESC))
                //制造分页查询
                .withPageable(PageRequest.of(pageNum, 8))
                //高亮显示
                /*.withHighlightFields(new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("description").preTags("<em>").postTags("</em>")
                )*/.build();
        //底层获取了高亮值，但是没有返回
        Page<Blog> search = blogRepository.search(build);
        return search;
        /*System.out.println(search.getTotalElements());
        System.out.println(search.getTotalPages());
        //当前第几页
        System.out.println(search.getNumber());
        //每页多少数据
        System.out.println(search.getSize());
        for (Blog blog:
                search) {
            System.out.println(blog);
        }*/
    }


}
