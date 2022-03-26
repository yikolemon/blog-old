package com.yikolemon.pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@lombok.Data                              //get，set
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class Comment implements Serializable {
    private Long id;
    private String nickname;
    private String email;
    private String content;
    private Date createTime;
    /*和blog的关系*/
    private Long blogId;
    /*父子评论的关系*/
    private Long parentCommentId;
    private String parentNickname;

    private Boolean flag;//管理员还是用户
    private List<Comment> childComment;


}
