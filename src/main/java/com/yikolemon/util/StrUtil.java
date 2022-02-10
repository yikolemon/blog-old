package com.yikolemon.util;

public class StrUtil {
    public static boolean isEmpty(String str){
        if (str==null) return true;
        if (str.equals("")) return true;
        return false;
    }
}
