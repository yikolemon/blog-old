package com.yikolemon.service.chat.pojo;

import com.yikolemon.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatUser {
    private User user;
    private boolean unRead;
}
