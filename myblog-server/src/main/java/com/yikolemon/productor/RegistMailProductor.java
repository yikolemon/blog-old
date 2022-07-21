package com.yikolemon.productor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.yikolemon.config.QueueConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

@Component
public class RegistMailProductor {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*//mq测试
    @GetMapping("/send")
    @ResponseBody
    public void send() throws JsonProcessingException {
        this.sendRegist("123","123");
    }
    */

    public void sendRegist(String code,String email) throws JsonProcessingException {
        Map<String,String> params = new HashMap<>();
        params.put("code",code);
        params.put("email",email);
        //将对象转为JSON串
        Gson gson = new Gson();
        String msg = gson.toJson(params);
        //System.out.println(jsonString);
        rabbitTemplate.convertAndSend(QueueConfig.registExchange,QueueConfig.routingKey,msg);
    }

}
