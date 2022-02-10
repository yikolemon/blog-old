package com.yikolemon.service;

import com.yikolemon.pojo.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(long blogId);

    int saveComment(Comment comment);

    List<Comment> listDealComments(long blogId);


}
