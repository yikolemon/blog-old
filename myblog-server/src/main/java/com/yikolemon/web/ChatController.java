//package com.yikolemon.web;
//
//import com.google.gson.Gson;
//import com.yikolemon.chat.pojo.ChatUser;
//import com.yikolemon.pojo.CloudMessage;
//import com.yikolemon.pojo.User;
//import com.yikolemon.service.UserService;
//import com.yikolemon.util.RedisKeyUtil;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authz.annotation.RequiresUser;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import javax.annotation.Resource;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.time.Duration;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Set;
//
//@Controller
//@RequiresUser
//public class ChatController {
//
//    @Resource
//    private UserService userService;
//
//    private final Logger logger= LoggerFactory.getLogger(ChatController.class);
//
//    @GetMapping("/chat")
//    public String chat(Model model){
//        //获取用户信息
//        List<User> allUser = userService.getAllUser();
//        String myUsername = (String) SecurityUtils.getSubject().getPrincipal();
//        model.addAttribute("myUsername",myUsername);
//        String key = RedisKeyUtil.getUnreceiveFromSetKey(myUsername);
//        Boolean lockBool = getRedisTemplate().opsForValue().setIfAbsent(RedisKeyUtil.getRedisLock(myUsername), "value", Duration.ofSeconds(10));
//        Set<String> executeRes=null;
//        if (lockBool){
//            executeRes = getRedisTemplate().opsForSet().members(key);
//        }else {
//            getRedisTemplate().delete(RedisKeyUtil.getRedisLock(myUsername));
//            logger.error("controller获取锁失败");
//            return "error/500";
//        }
//        getRedisTemplate().delete(RedisKeyUtil.getRedisLock(myUsername));
//        LinkedList<ChatUser> userList=new LinkedList<>();
//        for (User u :allUser) {
//            if (executeRes!=null&&executeRes.contains(u.getUsername())){
//                userList.addFirst(new ChatUser(u,true));
//            }else {
//                userList.addLast(new ChatUser(u,false));
//            }
//        }
//        model.addAttribute("userList",userList);
//        return "chat";
//    }
//
//
//    @GetMapping("/chat/{from}")
//    @ResponseBody
//    public List<String> pullMsg(@PathVariable String from){
//        String username = (String) SecurityUtils.getSubject().getPrincipal();
//        //加锁
//        Boolean lockBool = getRedisTemplate().opsForValue().setIfAbsent(RedisKeyUtil.getRedisLock(username), "value", Duration.ofSeconds(10));
//        String key = RedisKeyUtil.getUnreceiveMsgListKey(username, from);
//        //取出未读消息
//        List<String> range = getRedisTemplate().opsForList().range(key, 0, -1);
//        getRedisTemplate().delete(key);
//        getRedisTemplate().opsForSet().remove(RedisKeyUtil.getUnreceiveFromSetKey(username),from);
//        //解锁
//        getRedisTemplate().delete(RedisKeyUtil.getRedisLock(username));
//        return range;
//    }
//
//    @GetMapping("/chatDel/{from}")
//    @ResponseBody
//    public void delUnrec(@PathVariable String from){
//        String username = (String) SecurityUtils.getSubject().getPrincipal();
//        //加锁
//        Boolean lockBool = getRedisTemplate().opsForValue().setIfAbsent(RedisKeyUtil.getRedisLock(username), "value", Duration.ofSeconds(10));
//        String key = RedisKeyUtil.getUnreceiveMsgListKey(username, from);
//        getRedisTemplate().delete(key);
//        getRedisTemplate().opsForSet().remove(RedisKeyUtil.getUnreceiveFromSetKey(username),from);
//        //解锁
//        getRedisTemplate().delete(RedisKeyUtil.getRedisLock(username));
//    }
//
//    @GetMapping("/chatHistory/public")
//    @ResponseBody
//    public String pullPublicHistory (){
//        //拉取公共聊天记录
//        List<CloudMessage> range = getSepcisalRedisTemplate().opsForList().range(RedisKeyUtil.getCloudPublicMsgSaveKey(), 0, -1);
//        return new Gson().toJson(range);
//
//    }
//
//    @GetMapping("/chatHistory/p2p/{toName}")
//    @ResponseBody
//    public String pullP2PHistory(@PathVariable String toName){
//        String username = (String) SecurityUtils.getSubject().getPrincipal();
//        String key = RedisKeyUtil.getCloudP2PMsgSaveKey(username, toName);
//        List<CloudMessage> range = getSepcisalRedisTemplate().opsForList().range(key, 0, -1);
//        return new Gson().toJson(range);
//    }
//
//
//
//    private RedisTemplate getSepcisalRedisTemplate(){
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
//        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
//        return redisTemplate;
//    }
//
//    private RedisTemplate getRedisTemplate(){
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }
//
//
//}
