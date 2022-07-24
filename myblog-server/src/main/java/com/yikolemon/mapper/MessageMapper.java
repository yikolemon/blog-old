package com.yikolemon.mapper;

import com.yikolemon.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageMapper {

    //获取所有留言
    List<Message> getParentMessages();

    //删除本留言和所有子留言
    int deleteMessage(long id);

    int addMessage(Message message);

    //获取子留言
    List<Message> getChildMessages(long id);

    Boolean getEmailFlag(long id);

    //获取邮件,如果开启了回复才能获取到
    String getEmailIfFlag(long id);

}
