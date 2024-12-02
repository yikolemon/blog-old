//package com.yikolemon.service;
//
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * @author yikolemon
// */
//public interface MessageService {
//
//    @Cacheable(key = "'getParentMessages'")
//    public List<Message> getParentMessages();
//
//    @CacheEvict(allEntries = true)
//    public int deleteMessage(long id) {
//        return messageMapper.deleteMessage(id);
//    }
//
//    @CacheEvict(allEntries = true)
//    public int addMessage(Message message) {
//        message.setCreateTime(new Date());
//        return messageMapper.addMessage(message);
//    }
//
//    @Cacheable(key = "'getChildMessages'+#id")
//    public List<Message> getChildMessages(long id) {
//        return messageMapper.getChildMessages(id);
//    }
//
//    @Cacheable(key = "'getEmailFlag'+#id")
//    public Boolean getEmailFlag(long id) {
//        return messageMapper.getEmailFlag(id);
//    }
//
//    @Cacheable(key = "'getEmailIfFlag'+#id")
//    public String getEmailIfFlag(long id) {
//        return messageMapper.getEmailIfFlag(id);
//    }
//
//}
