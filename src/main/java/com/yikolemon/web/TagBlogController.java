package com.yikolemon.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yikolemon.queue.IndexBlog;
import com.yikolemon.queue.IndexTag;
import com.yikolemon.service.BlogServiceImpl;
import com.yikolemon.service.TagServiceImpl;
import com.yikolemon.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TagBlogController {

    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    private BlogServiceImpl blogService;

    int pageSize= PageUtils.getPageSize();

    @GetMapping("/tags/{tagId}")
    public String tagShowBlog(@PathVariable long tagId, @RequestParam(defaultValue = "1")int pageNum, Model model){
        List<IndexTag> tags = tagService.getAllIndexTags();
        if (tags.size()==0) return "error/0";
        model.addAttribute("tags",tags);
        if (tagId==-1){
            tagId=tags.get(0).getId();
        }else {
            boolean flag=false;
            for (IndexTag tag:tags) {
                if (tag.getId()==tagId){
                    flag=true;
                    break;
                }
            }
            if (flag==false) return "error/404";
        }
        model.addAttribute("tagId",tagId);
        PageHelper.startPage(pageNum,pageSize);
        List<IndexBlog> blogs = blogService.listBlogsByTagId(tagId);
        if (blogs.size()==0) pageNum=1;
        PageHelper.startPage(pageNum,pageSize);
        blogs = blogService.listBlogsByTagId(tagId);
        PageInfo<IndexBlog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("pageInfo",pageInfo);
        return "tags";

    }

}
