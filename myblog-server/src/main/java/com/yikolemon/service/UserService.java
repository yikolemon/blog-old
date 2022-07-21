package com.yikolemon.service;

import com.yikolemon.pojo.User;

public interface UserService {
    User checkUser(String username, String password);

    User getUser(long id);

    User getUserByUsername(String name);

    Boolean isAdmin(String username);

    long getIdByName(String name);

    Boolean hasUserByEmail(String email);

    int saveUser(User user);

    String getSaltByUsername(String username);

    int updateNicknameById(long id, String nickname);

}
