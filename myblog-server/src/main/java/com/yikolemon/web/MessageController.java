package com.yikolemon.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yikolemon.pojo.Message;
import com.yikolemon.pojo.User;
import com.yikolemon.service.MessageService;
import com.yikolemon.service.UserService;
import com.yikolemon.service.mail.EmailSenderThreadPool;
import com.yikolemon.util.PageUtils;
import com.yikolemon.util.SensitiveUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/messages")
@RequiresUser
public class MessageController {

    int MsgSize= PageUtils.getMessageSize();

    @Resource
    private MessageService messageService;

    @Resource
    private SensitiveUtil sensitiveUtil;

    @Resource
    private UserService userService;

    @Resource
    private EmailSenderThreadPool emailSenderThreadPool;

    @GetMapping("")
    public String loginPage(@RequestParam(defaultValue = "1") int pageNum, Model model){
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.getUserByUsername(principal);
        user.setPassword(null);
        model.addAttribute("user",user);
        PageHelper.startPage(pageNum,MsgSize);
        List<Message> messages = messageService.getParentMessages();
        if (messages.size()==0){
            pageNum=1;
            PageHelper.startPage(pageNum,MsgSize);
            messages = messageService.getParentMessages();
        }
        for (Message message:messages) {
            //子留言
            message.setChildMessage(messageService.getChildMessages(message.getId()));
        }
        PageInfo<Message> pageInfo = new PageInfo<>(messages);
        model.addAttribute("pageInfo",pageInfo);
        return "messages";
    }

    @PostMapping("")
    public String addMessage(Message message, String replayId, HttpServletRequest request, RedirectAttributes attributes) {
        String content = message.getContent();
        if (sensitiveUtil.containsSensitiveWord(content)){
            attributes.addFlashAttribute("msg","留言包含敏感词汇");
            return "redirect:/messages";
        }
        //1.判断是不是parent留言
        Boolean isParent = message.getIsParent();
        if (!isParent){
            //是回复留言,判断父留言是否需要提醒
            long replayIdLong = Long.parseLong(replayId);
            String email = messageService.getEmailIfFlag(replayIdLong);
            //不为空说明就开启了
            if (email!=null){
                Message parentMsg = messageService.getById(replayIdLong);
                //发送邮件
                emailSenderThreadPool.sendReplayEmailAsync(email, message.getNickname(),
                        parentMsg.getContent(), message.getContent());
            }
            //不然就正常存储
        }

        //不是的话，进行普通的消息的存储
        messageService.addMessage(message);
        //重定向到post前原本的页面
        String retUrl = request.getHeader("Referer");
        System.out.println(retUrl);
        return "redirect:"+retUrl;
    }

}
