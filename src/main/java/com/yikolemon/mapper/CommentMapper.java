package com.yikolemon.mapper;

import com.yikolemon.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {

    List<Comment> listCommentByBlogId(long blogId);

    int saveComment(Comment comment);

}
