package com.yikolemon.service;

import com.yikolemon.mapper.CommentMapper;
import com.yikolemon.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> listCommentByBlogId(long blogId) {
        return commentMapper.listCommentByBlogId(blogId);
    }

    @Override
    public int saveComment(Comment comment) {
        if (comment.getParentCommentId()!=null) {
            if (comment.getParentCommentId() == -1) {
                comment.setParentCommentId(null);
            }
        }
        comment.setCreateTime(new Date());
        return commentMapper.saveComment(comment);


    }

    @Override
    public List<Comment> listDealComments(long blogId) {
        List<Comment> comments = commentMapper.listCommentByBlogId(blogId);
        if (comments.size()==1) return comments;
        List<Comment> commentList=new ArrayList<>();

        for (int i = comments.size()-1; i >=0 ; i--) {
            Comment comment = comments.get(i);
            if (comment.getParentCommentId()==null) {
                commentList.add(comment);
                comments.remove(i);
            }
        }
        for (Comment father:commentList) {
            Long id = father.getId();
            List<Comment> childlist=new ArrayList<>();
            HashSet set=new HashSet();
            set.add(id);
            for (int i = comments.size()-1; i >=0 ; i--) {
                if (set.contains(comments.get(i).getParentCommentId())){
                    set.add(comments.get(i).getId());
                    childlist.add(comments.get(i));
                    comments.remove(i);
                }
            }
            if (childlist.size()!=0) father.setChildComment(childlist);
        }
        return commentList;
    }
}
