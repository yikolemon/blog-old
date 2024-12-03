package com.yikolemon.web;

import com.yikolemon.service.DataService;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @Resource
    private DataService dataService;

    @GetMapping("/about")
    public String about(Model model){
        long uv = dataService.lastSevenDayUv();
        long dau = dataService.lastSevenDayDau();
        model.addAttribute("uv",uv);
        model.addAttribute("dau",dau);
        return "about";
    }

}
