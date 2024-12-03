package com.yikolemon.service;

import com.yikolemon.pojo.Message;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;
import java.util.List;

/**
 * @author yikolemon
 */
public interface MessageService {


    @Cacheable(key = "'getParentMessages'")
    List<Message> getParentMessages();

    @CacheEvict(allEntries = true)
    int deleteMessage(long id);

    @CacheEvict(allEntries = true)
    int addMessage(Message message);

    @Cacheable(key = "'getChildMessages'+#id")
    List<Message> getChildMessages(long id);

    @Cacheable(key = "'getEmailFlag'+#id")
    Boolean getEmailFlag(long id);

    @Cacheable(key = "'getEmailIfFlag'+#id")
    String getEmailIfFlag(long id);

    @Cacheable(key = "'getById'+#id")
    Message getById(long id);

}
