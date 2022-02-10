package com.yikolemon.service;

import com.yikolemon.pojo.User;

public interface UserService {
    User checkUser(String username,String password);

    User getUser(long id);
}
