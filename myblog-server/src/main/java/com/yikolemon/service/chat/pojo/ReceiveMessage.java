package com.yikolemon.service.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveMessage {

    private boolean publicChat;

    private String to;

    private String msg;

}
