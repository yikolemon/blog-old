package com.yikolemon.consumer;

import com.google.gson.Gson;
import com.yikolemon.pojo.CloudMessage;
import com.yikolemon.util.RedisKeyUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class CloudMsgSaveConsumer {


    @Autowired
    private RedisTemplate redisTemplate;


    private RedisTemplate getRedisTemplate(){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }

    @RabbitListener(queues = "cloudMsgQueue")
    public void saveMsgToCloud(Message message){
        String msg = new String(message.getBody());
        CloudMessage cloudMessage = new Gson().fromJson(msg, CloudMessage.class);
        if (cloudMessage.getIsPublic()){
            getRedisTemplate().opsForList().rightPush(RedisKeyUtil.getCloudPublicMsgSaveKey(),cloudMessage);
        }else {
            //p2p,其中from和to已经拍好的顺序
            getRedisTemplate().opsForList().rightPush(RedisKeyUtil.getCloudP2PMsgSaveKey(cloudMessage.getFrom(),cloudMessage.getTo()),cloudMessage);
        }
    }



}
