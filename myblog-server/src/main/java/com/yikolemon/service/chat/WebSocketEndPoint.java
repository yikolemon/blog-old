//package com.yikolemon.service.chat;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//import com.yikolemon.service.chat.pojo.CloudMessage;
//import com.yikolemon.service.chat.pojo.ReceiveMessage;
//import com.yikolemon.service.chat.pojo.SendMessage;
//import com.yikolemon.config.WebSocketConfig;
//import com.yikolemon.productor.ChatSaveProductor;
//import com.yikolemon.util.ApplicationContextUtils;
//import com.yikolemon.util.RedisKeyUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.redis.core.RedisOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.SessionCallback;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.stereotype.Component;
//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.time.Duration;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicInteger;
//
//
//@ServerEndpoint(value = "/chatSocket",configurator = WebSocketConfig.class)  /*, configurator = GetHttpSessionConfigurator.class*/
//@Component
//public class WebSocketEndPoint {
//
//
//    private String username;
//
//    private ChatSaveProductor chatSaveProductor;
//
//    private static final Logger logger=LoggerFactory.getLogger(WebSocketEndPoint.class);
//
//    private static AtomicInteger onlineNum=new AtomicInteger(0);
//
//    private static Map<String,WebSocketEndPoint> map=new ConcurrentHashMap<>();
//
//    private RedisTemplate redisTemplate;
//    /**
//     * 用来给客户端发送消息
//     */
//    private Session session;
//
//
//    @OnOpen
//    public void onOpen(Session session, EndpointConfig endpointConfig) throws IOException {
//        this.session = session;
//        username = (String)session.getUserProperties().get("username");
//
//        Boolean lockBool = getRedisTemplate().opsForValue().setIfAbsent(RedisKeyUtil.getRedisLock(username), "value", Duration.ofSeconds(10));
//        if (lockBool){
//            //加锁成功
//            if (!map.containsKey(username)) {
//                //第一次连接
//                map.put(username,this);
//                int i = onlineNum.incrementAndGet();
//            }else {
//                session.close();
//            }
//        }else {
//            logger.error("获取锁失败");
//        }
//        getRedisTemplate().delete(RedisKeyUtil.getRedisLock(username));
//        //注入productor
//        chatSaveProductor= ApplicationContextUtils.getApplicationContext().getBean(ChatSaveProductor.class);
//        //进行广播通知
//        sendToAll(SendMessage.systemPublicMsg(onlineNum.get()));
//    }
//
//    @OnClose
//    public void onClose () throws IOException {
//        Boolean lockBool = getRedisTemplate().opsForValue().setIfAbsent(RedisKeyUtil.getRedisLock(username), "value", Duration.ofSeconds(10));
//        if (lockBool){
//            onlineNum.decrementAndGet();
//            map.remove(username);
//        }else {
//            //加锁失败了
//            logger.error("获取锁失败");
//        }
//        getRedisTemplate().delete(RedisKeyUtil.getRedisLock(username));
//        //广播通知
//        sendToAll(SendMessage.systemPublicMsg(onlineNum.get()));
//    }
//
//
//    @OnMessage
//    public void onMessage(String message){
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            ReceiveMessage msg = objectMapper.readValue(message, ReceiveMessage.class);
//            //获取接收信息的用户
//            if (msg.isPublicChat()){
//                //公共聊天
//                SendMessage sendMessage = SendMessage.userPublicMsg(username, msg.getMsg());
//                //然后广播发送
//                sendToAllExcpetMe(username,sendMessage);
//                //rabbitmq保存记录
//                chatSaveProductor.saveCloudMsg(CloudMessage.getPublicCloudMsg(username,msg.getMsg()));
//            }else {
//                //P2P聊天
//                //对to加锁
//                Boolean lockBool = getRedisTemplate().opsForValue().setIfAbsent(RedisKeyUtil.getRedisLock(msg.getTo()), "value", Duration.ofSeconds(10));
//                if (lockBool) {
//                    getRedisTemplate().execute(new SessionCallback() {
//                        @Override
//                        public Object execute(RedisOperations operations) throws DataAccessException {
//                            operations.multi();
//                            operations.opsForSet().add(RedisKeyUtil.getUnreceiveFromSetKey(msg.getTo()),username);
//                            operations.opsForList().rightPush(RedisKeyUtil.getUnreceiveMsgListKey(msg.getTo(),username),msg.getMsg());
//                            operations.exec();
//                            return null;
//                        }
//                    });
//                    if (map.containsKey(msg.getTo())) {
//                        //说明在线,告诉他已经发送数据了,让他去拉取
//                        SendMessage sendMessage = SendMessage.userP2PMsg(username,msg.getMsg());
//                        sendToPerson(sendMessage,msg.getTo());
//                    }else {
//                        //不在线,等待拉取
//                        //不能使用异步,会出现数据问题
//                        //chatSaveProductor.unReceiveCloudMsg(CloudMessage.getP2PCloudMsg(username,msg.getTo(),msg.getMsg()));
//                    }
//                }else {
//                    //获取锁失败了
//                    logger.error("获取锁失败");
//                }
//                getRedisTemplate().delete(RedisKeyUtil.getRedisLock(msg.getTo()));
//                //插入到历史聊天中,rabbitmq
//                chatSaveProductor.saveCloudMsg(CloudMessage.getP2PCloudMsg(username,msg.getTo(),msg.getMsg()));
//            }
//        } catch (JsonProcessingException e) {
//            logger.error("接收客户端的消息，转换出错了！");
//            e.printStackTrace();
//        } catch (IOException e) {
//            logger.error("发送消息失败了");
//        }
//    }
//
//    /*向除了自己的所有人发送*/
//    public static void sendToAllExcpetMe(String username,SendMessage sendMessage) throws IOException {
//        for (WebSocketEndPoint socketEndPoint:map.values()){
//            if (socketEndPoint.username!=username)
//            socketEndPoint.session.getBasicRemote().sendText(new Gson().toJson(sendMessage));
//        }
//    }
//
//    /*向除了自己的所有人发送*/
//    public static void sendToAll(SendMessage sendMessage) throws IOException {
//        for (WebSocketEndPoint socketEndPoint:map.values()){
//            socketEndPoint.session.getBasicRemote().sendText(new Gson().toJson(sendMessage));
//        }
//    }
//
//    public static void sendToPerson(SendMessage sendMessage,String to) throws IOException {
//        WebSocketEndPoint socketEndPoint = map.get(to);
//        socketEndPoint.session.getBasicRemote().sendText(String.valueOf(new Gson().toJson(sendMessage)));
//    }
//
//
//    private RedisTemplate getRedisTemplate(){
//        if(redisTemplate == null){    //每个连接池的连接都要获得RedisTemplate
//            redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
//        }
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }
//
//
//}
