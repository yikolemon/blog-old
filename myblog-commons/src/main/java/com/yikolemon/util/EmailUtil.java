package com.yikolemon.util;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailUtil {

    //邮箱格式校验
    public static boolean mailConfirm(String mail){
        Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }

    public static String getRegistMailStr(String code, TemplateEngine engine){
        //通过code生成邮件
        Context context=new Context();
        context.setVariable("code",code);
        String process = engine.process("/mail/regist_mail", context);
        return process;
    }

    public static String getReplayMailStr(String nickname,String content,String replay,TemplateEngine engine){
        Context context=new Context();
        context.setVariable("nickname",nickname);
        context.setVariable("content",content);
        context.setVariable("replay",replay);
        String process = engine.process("/mail/replay_mail", context);
        return process;
    }

}
