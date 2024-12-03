package com.yikolemon.web;

import com.yikolemon.pojo.Comment;
import com.yikolemon.service.CommentService;
import com.yikolemon.service.CommentServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {

    @Resource
    private CommentService commentService;

    @GetMapping("/comments/{blogId}")
    public String commentlist(@PathVariable long blogId, Model model){

        List<Comment> comments = commentService.listDealComments(blogId);
        model.addAttribute("comments",comments);
        return "blog :: commentList";
    }


    @PostMapping("/comments")
    @Transactional
    public String getComment(Comment comment, HttpServletRequest request, HttpSession session){
        Object user = session.getAttribute("user");
        if (user!=null)
            comment.setFlag(true);
        if (comment.getParentCommentId()!=null) {
            if (comment.getParentCommentId() == -1)
                comment.setParentCommentId(null);
        }
        if (comment.getParentNickname().isEmpty())
            comment.setParentNickname(null);
        /*System.out.println(comment);*/
        commentService.saveComment(comment);
        return "redirect:/comments/"+comment.getBlogId();
    }


}
