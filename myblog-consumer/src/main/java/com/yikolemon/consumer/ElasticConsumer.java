package com.yikolemon.consumer;

import com.google.gson.Gson;
import com.yikolemon.mapper.elasticsearch.BlogRepository;
import com.yikolemon.pojo.Blog;
import com.yikolemon.service.ElasticsearchService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ElasticConsumer {

    @Autowired
    private ElasticsearchService elasticSearch;

    @RabbitListener(queues = "elasticAddQueue")
    public void receiceElasticAdd(Message message){
        String msg = new String(message.getBody());
        Blog blog = new Gson().fromJson(msg, Blog.class);
        elasticSearch.saveBlog(blog);
    }

    @RabbitListener(queues = "elasticDeleteQueue")
    public void receiceElasticDelete(Message message){
        String msg = new String(message.getBody());
        Long id = new Gson().fromJson(msg, Long.class);
        elasticSearch.deleteBlog(id.intValue());
    }

}

