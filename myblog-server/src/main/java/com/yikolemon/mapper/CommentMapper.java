package com.yikolemon.mapper;

import com.yikolemon.pojo.Blog;
import com.yikolemon.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {

    List<Comment> listCommentByBlogId(long blogId);

    int saveComment(Comment comment);

    List<Blog> listBlogIfHasComments();

    int deleteCommentById(long commentId);

    boolean hasParent(long commentId);

    List<Long> getChildIds(long commentId);

    long getBlogIdByCommentId(long commentId);
}
