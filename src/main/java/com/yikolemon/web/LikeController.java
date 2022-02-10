package com.yikolemon.web;

import com.yikolemon.pojo.Blog;
import com.yikolemon.service.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LikeController {

    @Autowired
    private BlogServiceImpl blogService;

    @Transactional
    @PostMapping("/like")
    public String updateLike(HttpServletRequest request, Model model){
        String blogIdstr = request.getParameter("blogId");
        Long blogId = Long.valueOf(blogIdstr);
        blogService.updateLike(blogId);
        Blog blog = blogService.getLike(blogId);
        model.addAttribute("blog",blog);
        return "blog :: like_fragment";
    }
}
