package com.yikolemon.util;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class CodeUtilTest {
    @Test
    public void test1(){
        String code = CodeUtil.getCode();
        System.out.println(code);
    }

    @Test
    public void test2(){
        String str="ceponliney@xx.com";
        Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        System.out.println(matcher.matches());
    }
}