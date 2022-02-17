package com.yikolemon.service;

import com.yikolemon.pojo.Blog;
import com.yikolemon.pojo.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(long blogId);

    int saveComment(Comment comment);

    List<Comment> listDealComments(long blogId);

    List<Blog> listBlogIfHasComments();

    void clearCommentsByBlogId(long blogId);

    void deleteCommentById(long id);

    long getBlogIdByCommentId(long commentId);

}
