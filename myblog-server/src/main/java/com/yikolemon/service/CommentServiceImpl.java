package com.yikolemon.service;

import com.yikolemon.mapper.CommentMapper;
import com.yikolemon.pojo.Blog;
import com.yikolemon.pojo.Comment;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@CacheConfig(cacheNames = "comments")
public class CommentServiceImpl implements CommentService{

    @Resource
    private CommentMapper commentMapper;

    @Override
    @Cacheable(key = "'listCommentByBlogId'+#blogId")
    public List<Comment> listCommentByBlogId(long blogId) {
        return commentMapper.listCommentByBlogId(blogId);
    }

    @Override
    @CacheEvict(allEntries = true)
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
    @Cacheable(key = "'listDealComments'+#blogId")
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
            for (int i = comments.size()-1; i >=0 ; i--) {
                if (id==comments.get(i).getParentCommentId()){
                    childlist.add(comments.get(i));
                    comments.remove(i);
                }
            }
            if (childlist.size()!=0) father.setChildComment(childlist);
        }
        return commentList;
    }

    /*针对楼中楼写的评论显示代码，由于删除的实现过于繁琐，涉及到依赖，改为一楼内不再盖楼，子评论地位互等*/
    /*@Override
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
    }*/

    @Override
    @Cacheable(key = "'listBlogIfHasComments'")
    public List<Blog> listBlogIfHasComments() {
        return commentMapper.listBlogIfHasComments();
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void clearCommentsByBlogId(long blogId) {
        List<Comment> commentList = commentMapper.listCommentByBlogId(blogId);
        for (Comment comment:commentList) {
            commentMapper.deleteCommentById(comment.getId());
        }
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteCommentById(long id) {
        boolean flag = commentMapper.hasParent(id);
        if (flag){
            commentMapper.deleteCommentById(id);
        }
        else {
            List<Long> childIds = commentMapper.getChildIds(id);
            childIds.add(id);
            for (long commentId:childIds) {
                commentMapper.deleteCommentById(commentId);
            }
        }
    }

    @Override
    @Cacheable(key = "'getBlogIdByCommentId'+#commentId")
    public long getBlogIdByCommentId(long commentId) {
        return commentMapper.getBlogIdByCommentId(commentId);
    }
}
