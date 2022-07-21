package com.yikolemon.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailClientTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public  void sendMail(){
        mailClient.sendMail("1142496307@qq.com","test","hi");
    }

    @Test
    public void sendHtml(){
        Context context=new Context();
        context.setVariable("code","T56XQX");
        String process = templateEngine.process("/mail/regist_mail", context);
        System.out.println(process);
        mailClient.sendMail("1142496307@qq.com","Yikolemon's Blog",process);
    }
}