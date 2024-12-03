package com.yikolemon.web.admin;

import com.yikolemon.pojo.Blog;
import com.yikolemon.pojo.Comment;
import com.yikolemon.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiresRoles("admin")
@Controller
@RequestMapping("/admin")
public class CommentAdminController {

    @Resource
    private CommentService commentService;

    @GetMapping("/comments")
    public String getCommentsBlog(Model model){
        List<Blog> blogs = commentService.listBlogIfHasComments();
        model.addAttribute("blogs",blogs);
        return "admin/comments";
    }

    @GetMapping("/comments/{id}/clear")
    public String clearByBlogId(@PathVariable long id,RedirectAttributes redirectAttributes){
        commentService.clearCommentsByBlogId(id);
        redirectAttributes.addFlashAttribute("message","更新成功");
        return "redirect:/admin/comments";
    }


    @GetMapping("/comments/{id}/edit")
    public String getCommentsByBlogId(@PathVariable long id,Model model){
        List<Comment> commentList = commentService.listDealComments(id);
        model.addAttribute("comments",commentList);
        return "admin/comment-edit";
    }

    @GetMapping("/comments/{id}/delete")
    public String deleteCommentById(@PathVariable long id, Model model,RedirectAttributes redirectAttributes){
        long blogId= commentService.getBlogIdByCommentId(id);
        commentService.deleteCommentById(id);
        redirectAttributes.addFlashAttribute("message","更新成功");
        return "redirect:/admin/comments/"+blogId+"/edit";
    }
}
