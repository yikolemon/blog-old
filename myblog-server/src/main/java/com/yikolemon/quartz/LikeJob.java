//package com.yikolemon.quartz;
//
//import com.yikolemon.service.BlogService;
//import com.yikolemon.service.LikeService;
//import com.yikolemon.util.RedisUtil;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.Response;
//import redis.clients.jedis.Transaction;
//
//import java.util.Map;
//
//public class LikeJob extends QuartzJobBean {
//
//    @Autowired
//    private LikeService likeService;
//    @Autowired
//    private BlogService blogService;
//
//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        //System.out.println("Hello Quartz");
//        //这里使用定时任务将like保存到数据库中
//        Jedis jedis = RedisUtil.getJedis();
//        Transaction multi = jedis.multi();
//        Response<Map<String, String>> response = multi.hgetAll("myblog-like");
//        Response<Map<String, String>> viewresponse = multi.hgetAll("myblog-view");
//        multi.del("myblog-like");
//        multi.del("myblog-view");
//        multi.exec();
//        multi.close();
//        jedis.close();
//        Map<String, String> likeMap = response.get();
//        if (likeMap==null||likeMap.size()==0){
//            System.out.println("redis的like数据为空");
//        }else {
//            for (Map.Entry<String, String> entry : likeMap.entrySet()) {
//                likeService.updateLike(Long.valueOf(entry.getKey()),Integer.valueOf(entry.getValue()));
//            }
//            System.out.println("like写入数据库成功");
//        }
//
//        Map<String, String> viewMap = viewresponse.get();
//        if (viewMap==null||viewMap.size()==0){
//            System.out.println("redis的view数据为空");
//        }else {
//            for (Map.Entry<String, String> entry : viewMap.entrySet()) {
//                blogService.updateView(Long.valueOf(entry.getKey()),Integer.valueOf(entry.getValue()));
//            }
//            System.out.println("view写入数据库成功");
//        }
//    }
//
//
//
//}
