package com.yikolemon.util;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.util.DigestUtils;

public class MD5Utils {
    public static String code(String str){
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("Iwaitkaisen", null, 1024);
        System.out.println(md5Hash.toHex());
    }

}
