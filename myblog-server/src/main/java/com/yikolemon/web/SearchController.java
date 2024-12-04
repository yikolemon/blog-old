package com.yikolemon.web;
;
import com.github.pagehelper.PageInfo;
import com.yikolemon.pojo.Blog;
import com.yikolemon.service.search.BlogFullTextCompoment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @author yikolemon
 */
@Controller
public class SearchController {

    @Resource
    BlogFullTextCompoment blogFullTextCompoment;


    @PostMapping("/search")
    public String search(@RequestParam String keyword, Model model, @RequestParam(defaultValue = "1") int pageNum){
        PageInfo<Blog> blogPageInfo = blogFullTextCompoment.searchPage(pageNum, keyword);
        model.addAttribute("pageInfo",blogPageInfo);
        model.addAttribute("keyword",keyword);
//        Page<Blog> page = elasticService.searchBlogs(keyword, pageNum);
//        List<Blog> content = page.getContent();
//        PageInfo<Blog> pageInfo = new PageInfo<>(content);
//        pageInfo.setTotal(page.getTotalElements());
//        pageInfo.setPageNum(page.getNumber());
//        pageInfo.setIsFirstPage(page.isFirst());
//        pageInfo.setIsLastPage(page.isLast());

        return "search";
    }

}
