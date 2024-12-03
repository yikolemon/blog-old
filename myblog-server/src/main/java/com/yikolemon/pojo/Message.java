package com.yikolemon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data                              //get，set
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class Message {

    private Long id;          // 留言ID
    private String content;   // 留言内容
    private String nickname;  // 留言者昵称
    private String replayNickname; // 回复对象昵称
    private Boolean isParent; // 是否为父留言，默认为TRUE
    private Long parentId;    // 父留言ID，如果是回复留言，则存储对应的父留言ID
    private String email;     // 留言者的邮箱（用于回复时邮件提醒）
    private Boolean emailFlag; // 是否开启邮件提醒，默认关闭
    private Date createTime; // 留言创建时间

    private List<Message> childMessage;

}
