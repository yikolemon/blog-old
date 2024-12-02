//package com.yikolemon.web;
//
//import com.github.pagehelper.PageInfo;
//import com.yikolemon.pojo.Blog;
//import com.yikolemon.service.ElasticsearchService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
//@Controller
//public class SearchController {
//
//    @Autowired
//    private ElasticsearchService elasticService;
//
//    @PostMapping("/search")
//    public String search(@RequestParam String keyword, Model model, @RequestParam(defaultValue = "0") int pageNum){
//        Page<Blog> page = elasticService.searchBlogs(keyword, pageNum);
//        List<Blog> content = page.getContent();
//        PageInfo<Blog> pageInfo = new PageInfo<>(content);
//        pageInfo.setTotal(page.getTotalElements());
//        pageInfo.setPageNum(page.getNumber());
//        pageInfo.setIsFirstPage(page.isFirst());
//        pageInfo.setIsLastPage(page.isLast());
//        model.addAttribute("pageInfo",pageInfo);
//        model.addAttribute("keyword",keyword);
//        return "search";
//    }
//
//}
