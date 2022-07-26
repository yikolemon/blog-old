package com.yikolemon.mapper.elasticsearch;

import com.yikolemon.pojo.Blog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends ElasticsearchRepository<Blog,Integer> {

}
