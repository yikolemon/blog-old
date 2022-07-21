package com.yikolemon.web;

import com.yikolemon.queue.ArchiveBlog;
import com.yikolemon.service.BlogService;
import com.yikolemon.service.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ArchiveBlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archiveShowBlog(Model model){
        Map<String, List<ArchiveBlog>> map = blogService.listBlogsArchive();
        model.addAttribute("map",map);
        int count = blogService.countBlog();
        model.addAttribute("count",count);
        return "archives";

    }


}
