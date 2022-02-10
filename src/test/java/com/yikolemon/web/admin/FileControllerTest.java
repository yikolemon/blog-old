package com.yikolemon.web.admin;

import com.yikolemon.util.QiniuCloudUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileControllerTest {

    @Test
    public void file() {
    }

    @Test
    public void getFile() {
    }

    @Test
    public void delete(){
        QiniuCloudUtil qiniuCloudUtil=new QiniuCloudUtil();
        String delete = qiniuCloudUtil.delete("bg.png");
        System.out.println(delete);
    }
}