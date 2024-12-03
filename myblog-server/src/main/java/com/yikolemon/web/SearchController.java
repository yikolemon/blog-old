package com.yikolemon.web;
;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @PostMapping("/search")
    public String search(@RequestParam String keyword, Model model, @RequestParam(defaultValue = "0") int pageNum){
//        Page<Blog> page = elasticService.searchBlogs(keyword, pageNum);
//        List<Blog> content = page.getContent();
//        PageInfo<Blog> pageInfo = new PageInfo<>(content);
//        pageInfo.setTotal(page.getTotalElements());
//        pageInfo.setPageNum(page.getNumber());
//        pageInfo.setIsFirstPage(page.isFirst());
//        pageInfo.setIsLastPage(page.isLast());
//        model.addAttribute("pageInfo",pageInfo);
//        model.addAttribute("keyword",keyword);
        return "search";
    }

}
