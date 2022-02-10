package com.yikolemon.util;

import java.util.List;

public class TagBlogUtils {
    public static long[] tagIds(String str){
        String[] strs = str.split(",");
        long[] tagIds=new long[strs.length];
        for (int i = 0; i < strs.length; i++) {
            tagIds[i]=Long.valueOf(strs[i]);
        }
        return tagIds;
    }
    public static String tagIds(long[] arr){
        if (arr.length==0) return "";
        String str=arr[0]+"";
        for (int i = 1; i < arr.length; i++) {
            str+=","+arr[i];
        }
        return str;
    }
}
