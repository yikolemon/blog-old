package com.yikolemon.productor;

import com.google.gson.Gson;
import com.yikolemon.chat.pojo.CloudMessage;
import com.yikolemon.config.QueueConfig;
import com.yikolemon.pojo.Blog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatSaveProductor {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*新增消息*/
    public void saveCloudMsg(CloudMessage message){
        String msg = new Gson().toJson(message);
        rabbitTemplate.convertAndSend(QueueConfig.cloudMsgExchang,QueueConfig.cloudMsgkey,msg);
    }



}
