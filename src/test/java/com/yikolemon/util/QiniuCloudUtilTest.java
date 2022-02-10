package com.yikolemon.util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class QiniuCloudUtilTest {

    @Test
    public void getUpToken() {
    }

    @Test
    public void upload() throws IOException {
        QiniuCloudUtil qiniuCloudUtil=new QiniuCloudUtil();
        String upload = qiniuCloudUtil.upload("C:\\Users\\Yikolemon\\Pictures\\1.png", "1.png");
        System.out.println(upload);
    }

    @Test
    public void put64image() {
    }
}