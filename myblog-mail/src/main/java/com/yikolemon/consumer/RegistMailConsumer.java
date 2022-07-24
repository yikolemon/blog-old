package com.yikolemon.consumer;


import com.google.gson.Gson;
import com.yikolemon.util.MailClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RegistMailConsumer {

    @Autowired
    private MailClient mailClient;

    @RabbitListener(queues = "registQueue")
    public void receiveRegist(Message message){
        String msg = new String(message.getBody());
        HashMap<String,String> map = new Gson().fromJson(msg, HashMap.class);
        String code = map.get("code");
        String email = map.get("email");
        //System.out.println(code);
        //System.out.println(email);
        mailClient.sendRegistMail(email,code);
    }

    //rabbitmqListener监听线程
    @RabbitListener(queues = "replayQueue")
    public void receiveReplay(Message message){
        String msg = new String(message.getBody());
        HashMap<String,String> map = new Gson().fromJson(msg, HashMap.class);
        String replay = map.get("replay");
        String nickname = map.get("nickname");
        String email = map.get("email");
        String content = map.get("content");
        mailClient.sendReplayMail(email,nickname,content,replay);
    }

}
