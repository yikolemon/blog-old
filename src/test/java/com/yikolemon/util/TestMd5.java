package com.yikolemon.util;

import org.apache.shiro.crypto.hash.Md5Hash;

public class TestMd5 {

    public static void main(String[] args) {
       /*
       setBytes没有加密
       Md5Hash md5Hash = new Md5Hash();
       md5Hash.setBytes("123".getBytes());*/
        //使用构造方法加密
        Md5Hash md5Hash = new Md5Hash("123456");
        System.out.println(md5Hash.toHex());
        //md5加盐,第一个参数为原密码，后面一个参数为盐
        Md5Hash fuck = new Md5Hash("123", "fuck");
        System.out.println(fuck.toHex());
        //使用hash散列,最后一个参数就是散列的次数
        Md5Hash sanlie=new Md5Hash("123456",null,1024);
        System.out.println(sanlie.toHex());
    }
}