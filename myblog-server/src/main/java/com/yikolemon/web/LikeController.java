package com.yikolemon.web;

import com.yikolemon.pojo.Like;
import com.yikolemon.service.LikeService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LikeController {

    @Resource
    private LikeService LikeService;

    @Transactional
    @PostMapping("/like")
    public String updateLike(@RequestParam Long blogId, Model model){
        LikeService.updateLikeOne(blogId);
        Like like = LikeService.getLike(blogId);
        model.addAttribute("like",like);
        return "blog :: like_fragment";
    }
}
