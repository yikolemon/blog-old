package com.yikolemon.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yikolemon.pojo.User;
import com.yikolemon.productor.RegistMailProductor;
import com.yikolemon.result.Result;
import com.yikolemon.service.UserService;
import com.yikolemon.util.CodeUtil;
import com.yikolemon.util.EmailUtil;
import com.yikolemon.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import javax.servlet.http.HttpServletRequest;


@Controller
public class RegistController {

/*    @Autowired
    private MailClient mailClient;*/

    @Autowired
    private UserService userService;

    @Autowired
    private RegistMailProductor mailProductor;

    //注册页面跳转接口
    @GetMapping("/regist")
    public String toRegist(){
        return "regist";
    }

/*    @GetMapping("/send")
    @ResponseBody
    public String  test() throws JsonProcessingException {
        mailProductor.sendReplay("123","123","123","1142496307@qq.com");
        return null;
    }*/

    @ResponseBody
    @PostMapping("/regist/sendMail")
    public String sendMail(HttpServletRequest request,String email,String username) throws JsonProcessingException {
        //编写检查代码
        //1.获取ip
        if(!EmailUtil.mailConfirm(email)){//邮箱格式不正确
            return Result.fall("邮箱格式不正确");
        }
        //检查邮箱是否已经被注册
        if (userService.getUserByUsername(username)!=null){
            return Result.fall("此用户名已被注册");
        }
        if (userService.hasUserByEmail(email)!=null){//不为空则已经被注册
            return Result.fall("此邮箱已被注册");
        }
        String remoteAddr = request.getRemoteAddr();
        String isExpired = "isExpired:" +remoteAddr;
        String timeLimit = "timeLimit:"+remoteAddr;
        Jedis jedis = RedisUtil.getJedis();
        String s = jedis.get(isExpired);
        if (s!=null){
            //超过次数，失败了
            jedis.close();
            return Result.fall("操作频繁，请稍后再试");
        }
        //还没有超过限制
        Long incr = jedis.incr(timeLimit);
        jedis.expire(timeLimit,21600);
        //System.out.println(remoteAddr);
        //System.out.println(isExpired);
        //System.out.println(timeLimit);
        if (incr>3){
            //此次为超出的次数,设置超时时间为6小时
            jedis.set(isExpired,"1",new SetParams().nx().ex(21600));
            jedis.del(timeLimit);
            jedis.close();
            return Result.fall("操作频繁，请稍后再试");
        }
        else {
            //没超过限制，发送邮件
            //System.out.println(email);
            String code=CodeUtil.getCode();
            //邮件发送指令,使用mailProductor发送给Mq
            mailProductor.sendRegist(code,email);
            jedis.set(email,code,new SetParams().ex(120));
            //返回
            jedis.close();
            return Result.suc();
        }
    }

    //注册接口
    @PostMapping("/regist")
    public String regist(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String email,
                         @RequestParam String code,
                         RedirectAttributes attributes){

        Jedis jedis = RedisUtil.getJedis();
        String s = jedis.get(email);
        if (code==null||!code.equals(s)){
            //不匹配，直接返回
            attributes.addFlashAttribute("username",username);
            attributes.addFlashAttribute("password",password);
            attributes.addFlashAttribute("email",email);
            attributes.addFlashAttribute("message","验证码错误");
            return "redirect:/regist";
        }
        if (userService.getUserByUsername(username)!=null){
            attributes.addFlashAttribute("message","此用户名已被注册");
            return "redirect:/regist";
        }
        //code匹配了
        //进行存储
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        userService.saveUser(user);
        //重定向到登录页面
        return "redirect:/login";
    }
}
