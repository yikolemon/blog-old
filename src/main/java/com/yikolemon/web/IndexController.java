package com.yikolemon.web;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yikolemon.NotFoundException;
import com.yikolemon.pojo.Blog;
import com.yikolemon.pojo.Tag;
import com.yikolemon.pojo.Type;
import com.yikolemon.pojo.User;
import com.yikolemon.queue.IndexBlog;
import com.yikolemon.queue.IndexTag;
import com.yikolemon.queue.IndexType;
import com.yikolemon.queue.RightTopBlog;
import com.yikolemon.service.*;
import com.yikolemon.util.PageUtils;
import com.yikolemon.util.TopConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class IndexController {

    @Autowired
    private BlogServiceImpl blogService;

    @Autowired
    private TypeServiceImpl typeService;

    @Autowired
    private  TagServiceImpl tagService;

    @Autowired
    private UserServiceImpl userService;

    int pageSize= PageUtils.getPageSize();

    int typeSize= TopConfig.getTypeSize();

    int tagSize=TopConfig.getTagSize();

    int blogSize=TopConfig.getBlogSize();

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") int pageNum,Model model) {
        PageHelper.startPage(pageNum,pageSize);
        List<IndexBlog> indexBlogs = blogService.listBlogsIndex();
        if (indexBlogs.size()==0)
            pageNum=1;
        PageHelper.startPage(pageNum,pageSize);
        indexBlogs = blogService.listBlogsIndex();
        PageInfo<IndexBlog> pageInfo = new PageInfo<>(indexBlogs);
        model.addAttribute("pageInfo",pageInfo);
        //获取总的记录数的方法  long total = pageInfo.getTotal();
        List<IndexType> typeTop = typeService.getTypeTop(typeSize);
        model.addAttribute("types",typeTop);
        List<IndexTag> tagTop = tagService.getTagTop(tagSize);
        model.addAttribute("tags",tagTop);
        List<RightTopBlog> recommendBlogs = blogService.listRecommendNewBlog(blogSize);
        model.addAttribute("recommendBlogs",recommendBlogs);
        List<RightTopBlog> viewBlogs = blogService.listMostviewBlog(blogSize);
        model.addAttribute("viewBlogs",viewBlogs);
        return "index";
    }

   @PostMapping("/search")
    public String search(@RequestParam String title, Model model,@RequestParam(defaultValue = "1") int pageNum){
        PageHelper.startPage(pageNum,pageSize);
       List<IndexBlog> indexBlogs = blogService.searchBlog(title);
       PageInfo<IndexBlog> pageInfo = new PageInfo<>(indexBlogs);
       model.addAttribute("pageInfo",pageInfo);
       model.addAttribute("title",title);
       return "search";
   }

    @GetMapping("/blog/{id}")
    @Transactional
    public String blog(@PathVariable long id,Model model) {
        int i = blogService.updateView(id);
        if (i==0) return "admin/500";
        Blog blog = blogService.getAndConvert(id);
        model.addAttribute("blog",blog);
        Long userId = blog.getUserId();
        User user = userService.getUser(userId);
        model.addAttribute("user",user);
        List<Tag> tags = tagService.getTagsByBlogId(blog.getId());
        System.out.println(tags);
        model.addAttribute("tags",tags);
        return "blog";
    }

}
