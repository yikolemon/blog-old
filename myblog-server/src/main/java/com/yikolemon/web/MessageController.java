//package com.yikolemon.web;
//
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import com.yikolemon.pojo.Message;
//import com.yikolemon.pojo.User;
//import com.yikolemon.productor.RegistMailProductor;
//import com.yikolemon.queue.IndexBlog;
//import com.yikolemon.service.MessageService;
//import com.yikolemon.service.UserService;
//import com.yikolemon.util.PageUtils;
//import com.yikolemon.util.SensitiveUtil;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authz.annotation.RequiresUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import org.springframework.web.servlet.view.RedirectView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//
//@Controller
//@RequestMapping("/messages")
//@RequiresUser
//public class MessageController {
//
//    int MsgSize= PageUtils.getMessageSize();
//
//    @Autowired
//    private MessageService messageService;
//
//    @Autowired
//    private SensitiveUtil sensitiveUtil;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private RegistMailProductor mailProductor;
//
//    @GetMapping("")
//    public String loginPage(@RequestParam(defaultValue = "1") int pageNum, Model model){
//        String principal = (String) SecurityUtils.getSubject().getPrincipal();
//        User user = userService.getUserByUsername(principal);
//        user.setPassword(null);
//        model.addAttribute("user",user);
//        PageHelper.startPage(pageNum,MsgSize);
//        List<Message> messages = messageService.getParentMessages();
//        if (messages.size()==0){
//            pageNum=1;
//            PageHelper.startPage(pageNum,MsgSize);
//            messages = messageService.getParentMessages();
//        }
//        for (Message message:messages) {
//            //子留言
//            message.setChildMessage(messageService.getChildMessages(message.getId()));
//        }
//        PageInfo<Message> pageInfo = new PageInfo<>(messages);
//        model.addAttribute("pageInfo",pageInfo);
//        return "messages";
//    }
//
//    @PostMapping("")
//    public String getMessage(Message message, String replayId, HttpServletRequest request, RedirectAttributes attributes) {
//
//        //1.判断是不是parent留言
//        String content = message.getContent();
//        if (sensitiveUtil.containsSensitiveWord(content)){
//            attributes.addFlashAttribute("msg","留言包含敏感词汇");
//            return "redirect:/messages";
//        }
//        Boolean isParent = message.getIsParent();
//        if (!isParent){
//            //是回复留言,判断父留言是否需要提醒
//            Long replayIdLong = Long.valueOf(replayId);
//            String email = messageService.getEmailIfFlag(replayIdLong);
//            //Boolean emailFlag = messageService.getEmailFlag(replayIdLong);
//            //不为空说明就开启了
//            if (email!=null){
//                //发送邮件
//                mailProductor.sendReplay(message.getContent(),message.getNickname(),message.getContent(),email);
//            }
//            //不然就正常存储
//        }
//
//        //不是的话，进行普通的消息的存储
//        messageService.addMessage(message);
//        //重定向到post前原本的页面
//        String retUrl = request.getHeader("Referer");
//        System.out.println(retUrl);
//        return "redirect:"+retUrl;
//    }
//
//}
