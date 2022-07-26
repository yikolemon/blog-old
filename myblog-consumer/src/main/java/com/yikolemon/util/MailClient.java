package com.yikolemon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailClient {

    private static final Logger logger=LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to,String subject,String content){
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            logger.error("发送邮件失败"+e.getMessage());
        }
    }

    public void sendRegistMail(String to,String code){
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Yikolemon's Blog 注册");
            String mailStr = EmailUtil.getRegistMailStr(code, templateEngine);
            helper.setText(mailStr,true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败"+e.getMessage());
        }catch (MailSendException e){
            logger.error("发送邮件失败"+e.getMessage());
        }
    }

    /**
     *
     * @param to    发送邮件地址
     * @param nickname 回复人昵称
     * @param content  回复对应的留言
     * @param replay    回复内容
     */
    public void sendReplayMail(String to,String nickname,String content,String replay){
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("回复通知 | Yikolemon'blog论坛");
            String mailStr = EmailUtil.getReplayMailStr(nickname, content, replay, templateEngine);
            helper.setText(mailStr,true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败"+e.getMessage());
        }catch (MailSendException e){
            logger.error("发送邮件失败"+e.getMessage());
        }
    }
}
