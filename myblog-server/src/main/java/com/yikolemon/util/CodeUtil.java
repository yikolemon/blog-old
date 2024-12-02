package com.yikolemon.util;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CodeUtil {

    private static Random r=new Random();

    public static String getCode(){
        StringBuilder builder=new StringBuilder();
        //2、定义一个for循环，循环n次，依次生成随机字符
        for (int i = 0; i < 6; i++) {
            boolean bool = r.nextBoolean();
            if (bool){
                //数字
                builder.append(r.nextInt(10));
            }
            else {
                char ch = (char) (r.nextInt(26) + 65);
                builder.append(ch);
            }
        }
        return builder.toString();
    }



}
