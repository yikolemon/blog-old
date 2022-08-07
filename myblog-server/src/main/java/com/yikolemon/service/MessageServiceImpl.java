package com.yikolemon.service;

import com.yikolemon.mapper.MessageMapper;
import com.yikolemon.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "message")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;


    @Override
    @Cacheable(key = "'getParentMessages'")
    public List<Message> getParentMessages() {
        return messageMapper.getParentMessages();
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteMessage(long id) {
        return messageMapper.deleteMessage(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int addMessage(Message message) {
        message.setCreateTime(new Date());
        return messageMapper.addMessage(message);
    }

    @Override
    @Cacheable(key = "'getChildMessages'+#id")
    public List<Message> getChildMessages(long id) {
        return messageMapper.getChildMessages(id);
    }

    @Override
    @Cacheable(key = "'getEmailFlag'+#id")
    public Boolean getEmailFlag(long id) {
        return messageMapper.getEmailFlag(id);
    }

    @Override
    @Cacheable(key = "'getEmailIfFlag'+#id")
    public String getEmailIfFlag(long id) {
        return messageMapper.getEmailIfFlag(id);
    }


}
