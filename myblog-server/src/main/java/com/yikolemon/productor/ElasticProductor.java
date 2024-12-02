//package com.yikolemon.productor;
//
//import com.google.gson.Gson;
//import com.yikolemon.config.QueueConfig;
//import com.yikolemon.pojo.Blog;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class ElasticProductor {
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    /*新增消息*/
//    public void sendElasticAdd(Blog blog){
//        Gson gson = new Gson();
//        String msg = gson.toJson(blog);
//        rabbitTemplate.convertAndSend(QueueConfig.elasticExchange,QueueConfig.addKey,msg);
//    }
//
//    /*删除消息*/
//    public void sendElasticDelete(long id){
//        Gson gson = new Gson();
//        String msg = gson.toJson(id);
//        rabbitTemplate.convertAndSend(QueueConfig.elasticExchange,QueueConfig.deleteKey,msg);
//    }
//
//}
