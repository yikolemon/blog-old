package com.yikolemon.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yikolemon.pojo.Type;
import com.yikolemon.queue.IndexBlog;
import com.yikolemon.queue.IndexType;
import com.yikolemon.service.BlogService;
import com.yikolemon.service.BlogServiceImpl;
import com.yikolemon.service.TypeService;
import com.yikolemon.service.TypeServiceImpl;
import com.yikolemon.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TypeBlogController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    int pageSize= PageUtils.getPageSize();


    @GetMapping("/types/{typeId}")
    public String typeShowBlog(@PathVariable long typeId,@RequestParam(defaultValue = "1")int pageNum,Model model){
        List<IndexType> types = typeService.getAllIndexTypes();
        if (types.size()==0)
        {
            return "error/0";
        }
        model.addAttribute("types",types);
        if (typeId==-1) {
            typeId=types.get(0).getId();
        }else{
            boolean flag=false;
            for (IndexType type:types) {
                if (type.getId()==typeId){
                    flag=true;
                    break;
                }
            }
            if (flag==false) return "error/404";
        }
        model.addAttribute("typeId",typeId);
        PageHelper.startPage(pageNum,pageSize);
        List<IndexBlog> blogs = blogService.listBlogsByTypeId(typeId);
        if (blogs.size()==0) pageNum=1;
        PageHelper.startPage(pageNum,pageSize);
        blogs = blogService.listBlogsByTypeId(typeId);
        PageInfo<IndexBlog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("pageInfo",pageInfo);
        return "types";
    }


}
