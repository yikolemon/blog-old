package com.yikolemon.web;

import com.yikolemon.pojo.User;
import com.yikolemon.result.Result;
import com.yikolemon.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresUser;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
//shiro授权注释
@RequestMapping("/me")
@RequiresUser
public class MeController {

    @Resource
    private UserService userService;

    @GetMapping("")
    public String loginPage(Model model){
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user",user);
        return "me";
    }

    @ResponseBody
    @PostMapping("/updateNickname")
    public String updateNickName(String id,String nickname){
        try{
            Long userId = Long.valueOf(id);
            userService.updateNicknameById(userId,nickname);
            return Result.suc();
        }
        catch (Exception e){
            return Result.fall("修改失败");
        }
    }
}
