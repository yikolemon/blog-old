//
//package com.yikolemon.config;
//import com.yikolemon.quartz.LikeJob;
//import org.quartz.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class QuartzConfig {
//
//    private static final String LikeJob = "LikeJob";
//
//    @Bean
//    public JobDetail likeJobDetail(){
//        return JobBuilder.newJob(LikeJob.class).//业务类
//                withIdentity(LikeJob).//给JobDetail起个名字
//                storeDurably().//没有Trigger关联也不需要删除该JobDetail起个名字
//                build();
//    }
//
//    @Bean
//    public Trigger quartzTrigger(){
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                //.withIntervalInSeconds(10)  //设置时间周期单位秒
//                //.withIntervalInSeconds(20) //20秒一次，用于测试
//                .withIntervalInHours(1)
//                .repeatForever();
//        return TriggerBuilder.newTrigger().forJob(likeJobDetail())
//                .withIdentity(LikeJob)
//                .withSchedule(scheduleBuilder)
//                .build();
//    }
//}