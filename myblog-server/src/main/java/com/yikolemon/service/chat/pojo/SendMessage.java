package com.yikolemon.service.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SendMessage {

    private boolean systemMsg;

    private int onlineNum;

    private boolean publicChat;

    private String from;

    private String msg;


    public static SendMessage userPublicMsg(String from,String msg){
        return new SendMessage(false,0,true,from,msg);
    }

    public static SendMessage systemPublicMsg(int onlineNum){
        return  new SendMessage(true,onlineNum,true,null,null);
    }

    public static SendMessage userP2PMsg(String from,String msg){
        return new SendMessage(false,0,false,from,msg);
    }

}
