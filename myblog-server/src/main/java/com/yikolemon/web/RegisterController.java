package com.yikolemon.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yikolemon.pojo.User;
import com.yikolemon.result.Result;
import com.yikolemon.service.mail.EmailSenderThreadPool;
import com.yikolemon.service.UserService;
import com.yikolemon.util.CodeUtil;
import com.yikolemon.util.EmailUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Controller
public class RegisterController {

    @Resource
    private UserService userService;

    @Resource
    private EmailSenderThreadPool emailSenderThreadPool;

    // 用于存储邮箱和验证码的内存缓存
    private final ConcurrentHashMap<String, String> emailCache = new ConcurrentHashMap<>();
    // 用于限制 IP 地址的请求频率
    private final ConcurrentHashMap<String, Integer> ipRequestCount = new ConcurrentHashMap<>();
    // 用于存储 IP 地址的过期时间（限制频率的冷却期）
    private final ConcurrentHashMap<String, Long> ipCoolDown = new ConcurrentHashMap<>();

    // 注册页面跳转接口
    @GetMapping("/regist")
    public String toRegist() {
        return "regist";
    }

    @ResponseBody
    @PostMapping("/regist/sendMail")
    public String sendMail(HttpServletRequest request, String email, String username) throws JsonProcessingException {
        // 检查邮箱格式
        if (!EmailUtil.mailConfirm(email)) {
            return Result.fall("邮箱格式不正确");
        }
        // 检查邮箱是否已注册
        if (userService.hasUserByEmail(email) != null) {
            return Result.fall("此邮箱已被注册");
        }
        // 检查用户名是否已注册
        if (userService.getUserByUsername(username) != null) {
            return Result.fall("此用户名已被注册");
        }

        // 获取 IP 地址
        String remoteAddr = request.getRemoteAddr();

        // 限制每个 IP 每小时的请求次数（模拟过期时间）
        long currentTime = System.currentTimeMillis();
        if (ipCoolDown.containsKey(remoteAddr) && currentTime - ipCoolDown.get(remoteAddr) < TimeUnit.HOURS.toMillis(1)) {
            return Result.fall("操作频繁，请稍后再试");
        }

        Integer requestCount = ipRequestCount.getOrDefault(remoteAddr, 0);
        if (requestCount >= 3) {
            // 超过最大请求次数，设置冷却期
            ipCoolDown.put(remoteAddr, currentTime);
            ipRequestCount.put(remoteAddr, 0); // 重置请求次数
            return Result.fall("操作频繁，请稍后再试");
        }

        // 没有超过限制，发送邮件
        String code = CodeUtil.getCode();
        emailSenderThreadPool.sendRegisterEmailAsync(email, code);
        // 将验证码存储在内存中，过期时间 2 分钟
        emailCache.put(email, code);
        ipRequestCount.put(remoteAddr, requestCount + 1);
        // 模拟过期机制，通过定时任务清除过期验证码
        new Thread(() -> {
            try {
                TimeUnit.MINUTES.sleep(2);
                emailCache.remove(email);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        return Result.suc();
    }

    // 注册接口
    @PostMapping("/regist")
    public String regist(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String email,
                         @RequestParam String code,
                         RedirectAttributes attributes) {

        // 检查验证码
        String cachedCode = emailCache.get(email);
        if (code == null || !code.equals(cachedCode)) {
            attributes.addFlashAttribute("username", username);
            attributes.addFlashAttribute("password", password);
            attributes.addFlashAttribute("email", email);
            attributes.addFlashAttribute("message", "验证码错误");
            return "redirect:/regist";
        }
        // 检查用户名是否已注册
        if (userService.getUserByUsername(username) != null) {
            attributes.addFlashAttribute("message", "此用户名已被注册");
            return "redirect:/regist";
        }
        // 用户信息保存
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        userService.saveUser(user);
        // 注册成功，重定向到登录页面
        return "redirect:/login";
    }
}
