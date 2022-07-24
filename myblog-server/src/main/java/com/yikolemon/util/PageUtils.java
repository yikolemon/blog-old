package com.yikolemon.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PageUtils {



    private static int MessageSize;
    private static int PageSize;

    public static int getPageSize(){
        return PageSize;
    }
    @Value("${pagesize}")
    public void setPageSize(int num){
        PageSize=num;
    }

    public static int getMessageSize() {
        return MessageSize;
    }

    @Value("${msgsize}")
    public  void setMessageSize(int messageSize) {
        MessageSize = messageSize;
    }

}
