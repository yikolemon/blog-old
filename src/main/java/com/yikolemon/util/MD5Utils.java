package com.yikolemon.util;

import org.springframework.util.DigestUtils;

public class MD5Utils {
    public static String code(String str){
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    public static void main(String[] args) {
        System.out.println(MD5Utils.code("123456"));
    }
}
