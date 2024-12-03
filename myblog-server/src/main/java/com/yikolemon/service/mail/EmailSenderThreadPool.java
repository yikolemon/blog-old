package com.yikolemon.service.mail;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class EmailSenderThreadPool {

    private ThreadPoolExecutor registerExecutor;

    private ThreadPoolExecutor replayExecutor;

    @Resource
    private MailClient mailClient;


    public EmailSenderThreadPool() {
        // 自定义线程池大小、队列容量等
        this.registerExecutor = new ThreadPoolExecutor(
                3,  // corePoolSize
                6, // maximumPoolSize
                60L, TimeUnit.SECONDS, // keepAliveTime
                new LinkedBlockingQueue<>(100) // 队列大小
        );
        this.replayExecutor = new ThreadPoolExecutor(
                3,  // corePoolSize
                6, // maximumPoolSize
                60L, TimeUnit.SECONDS, // keepAliveTime
                new LinkedBlockingQueue<>(100) // 队列大小
        );
    }

    public void sendRegisterEmailAsync(String to, String code) {
        registerExecutor.submit(() -> {
            mailClient.sendRegisterMail(to, code);
        });
    }

    public void sendReplayEmailAsync(String to, String nickName,
                                     String content, String replay){
        replayExecutor.submit(()->{
            mailClient.sendReplayMail(to, nickName, content, replay);
        });
    }

    public void shutdown() {
        registerExecutor.shutdown();
    }

}
