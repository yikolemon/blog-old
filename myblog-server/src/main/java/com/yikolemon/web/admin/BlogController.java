package com.yikolemon.web.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yikolemon.pojo.Blog;
import com.yikolemon.pojo.Tag;
import com.yikolemon.pojo.Type;
import com.yikolemon.queue.SearchBlog;
import com.yikolemon.service.*;
import com.yikolemon.util.MdImgConvertComponent;
import com.yikolemon.util.PageUtils;
import com.yikolemon.util.QiniuCloudOssComponent;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiresRoles("admin")
@Controller
@RequestMapping("/admin")
public class BlogController {

    @Resource
    private BlogService blogService;
    @Resource
    private TypeService typeService;
    @Resource
    private TagService tagService;
    @Resource
    private TagBlogService tagBlogService;
    @Resource
    private UserService userService;

    @Resource
    private MdImgConvertComponent mdImgConvertComponent;
//    @Resource
//    private ElasticProductor elasticProductor;

    int pageSize= PageUtils.getPageSize();



    @GetMapping("/blogs")
    public String blogsAdminIndex(Model model){
        PageHelper.startPage(1,pageSize);
        List<Blog> list = blogService.listBlogsAdmin();
        PageInfo<Blog> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo",pageInfo);
        List<Type> types = typeService.getAllTypes();
        model.addAttribute("types",types);
        return "admin/blogs";
    }

    @GetMapping("/blogs/input")
    public String input(Model model){
        List<Type> types = typeService.getAllTypes();
        model.addAttribute("types",types);
        List<Tag> tags = tagService.getAllTags();
        model.addAttribute("tags",tags);
        model.addAttribute("blog",new Blog());
        return "admin/blogs-input";
    }

    @GetMapping("/blogs/{id}/input")
    public String update(@PathVariable long id,Model model){
        List<Type> types = typeService.getAllTypes();
        model.addAttribute("types",types);
        List<Tag> tags = tagService.getAllTags();
        model.addAttribute("tags",tags);
        String tagIds = tagBlogService.StringTagIdsByBlogId(id);
        model.addAttribute("tagIds",tagIds);
        Blog blog = blogService.getBlog(id);
        model.addAttribute("blog",blog);
        return "admin/blogs-input";
    }

    @GetMapping("/blogs/{id}/delete")
    @Transactional
    public String delete(@PathVariable long id,Model model, RedirectAttributes redirectAttributes){
        tagBlogService.deleteTagByBlogId(id);
        int i = blogService.deleteBlog(id);
        if (i==1){
//            elasticProductor.sendElasticDelete(id);
            redirectAttributes.addFlashAttribute("message","操作成功");
        }
        else {
            redirectAttributes.addFlashAttribute("message","操作失败");
        }
        return "redirect:/admin/blogs";
    }


    @PostMapping("/blogs/search")
    public String search(Model model, SearchBlog blog, int page,HttpServletRequest request){
        PageHelper.startPage(page,pageSize);
        boolean published=true;
        String unpublished = request.getParameter("unpublished");
        if (unpublished.equals("true")) {
            published=false;
        }
        blog.setPublished(published);
        List<Blog> list = blogService.listAllBlogsSearch(blog);
        PageInfo<Blog> pageInfo = new PageInfo<>(list);
        /*System.out.println(pageInfo.isIsFirstPage());*/
        model.addAttribute("pageInfo",pageInfo);
        return "admin/blogs :: blogList";
    }

    @PostMapping("/blogs/input")
    @Transactional
    public String editBlog(Boolean ossImg, Blog blog,
                           RedirectAttributes redirectAttributes,HttpServletRequest request){
        if (Boolean.TRUE.equals(ossImg)){
            //需要对img存储到oss上
            String content = mdImgConvertComponent.replaceImagesWithOssPaths(blog.getContent());
            blog.setContent(content);
        }
        if (blog.getId()!=null){
            updateBlog(blog,redirectAttributes,request);
        }
        else {
            saveBlog(blog,redirectAttributes,request);
        }
//        elasticProductor.sendElasticAdd(blog);
        return "redirect:/admin/blogs";
    }

    public  void saveBlog(Blog blog, RedirectAttributes redirectAttributes,
                          HttpServletRequest request){
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        long id = userService.getIdByName(principal);
        blog.setUserId(id);
        blogService.saveBlog(blog);
        Long blogId = blog.getId();//id在存完之后才能取
        String tagIds = request.getParameter("tagIds");
        tagBlogService.saveTagBlogs(tagIds, blogId);
    }

    public void updateBlog(Blog blog, RedirectAttributes redirectAttributes, HttpServletRequest request){
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        long id = userService.getIdByName(principal);
        blog.setUserId(id);
        tagBlogService.deleteTagByBlogId(blog.getId());
        String tagIds = request.getParameter("tagIds");
        tagBlogService.saveTagBlogs(tagIds, blog.getId());
        blogService.updateBlog(blog);
    }


}
