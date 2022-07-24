package com.yikolemon.service;

import com.yikolemon.mapper.MessageMapper;
import com.yikolemon.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;


    @Override
    public List<Message> getParentMessages() {
        return messageMapper.getParentMessages();
    }

    @Override
    public int deleteMessage(long id) {
        return messageMapper.deleteMessage(id);
    }

    @Override
    public int addMessage(Message message) {
        message.setCreateTime(new Date());
        return messageMapper.addMessage(message);
    }

    @Override
    public List<Message> getChildMessages(long id) {
        return messageMapper.getChildMessages(id);
    }

    @Override
    public Boolean getEmailFlag(long id) {
        return messageMapper.getEmailFlag(id);
    }

    @Override
    public String getEmailIfFlag(long id) {
        return messageMapper.getEmailIfFlag(id);
    }


}
