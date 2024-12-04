package com.yikolemon.service.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudMessage implements Serializable {
    private Boolean isPublic;
    private String from;
    private String to;
    private String msg;

    public static CloudMessage getPublicCloudMsg(String from,String msg){
        return new CloudMessage(true,from,null,msg);
    }

    public static CloudMessage getP2PCloudMsg(String from,String to,String msg){
        int i = from.compareTo(to);
        if (i>0){
            String temp=from;
            from=to;
            to=temp;
        }
        return new CloudMessage(false,from,to,msg);
    }
}
