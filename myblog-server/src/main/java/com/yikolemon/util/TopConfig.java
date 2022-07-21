package com.yikolemon.util;

public class TopConfig {

    private static int TypeSize=6;
    private static int TagSize=6;
    private static int BlogSize=6;

    public static int getTypeSize() {
        return TypeSize;
    }

    public static void setTypeSize(int typeSize) {
        TypeSize = typeSize;
    }

    public static int getTagSize() {
        return TagSize;
    }

    public static void setTagSize(int tagSize) {
        TagSize = tagSize;
    }

    public static int getBlogSize() {
        return BlogSize;
    }

    public static void setBlogSize(int blogSize) {
        BlogSize = blogSize;
    }
}
