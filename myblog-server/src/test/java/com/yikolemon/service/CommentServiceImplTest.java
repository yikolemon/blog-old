package com.yikolemon.service;

import com.yikolemon.pojo.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest

public class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Test
    public void listCommentByBlogId() {
        List<Comment> comments = commentService.listCommentByBlogId((long) 188);
        for (Comment com:
             comments) {
            System.out.println(com);
        }
    }

    @Test
    public void saveComment() {
        Comment comment=new Comment();
        comment.setNickname("小王");
        comment.setContent("测试内容1");
        commentService.saveComment(comment);
    }

    @Test
    public void listDealComments(){
        List<Comment> comments = commentService.listDealComments(188);
        for (Comment com:
             comments) {
            System.out.println(com);
        }
    }
}