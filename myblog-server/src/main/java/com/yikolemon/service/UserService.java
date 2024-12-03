package com.yikolemon.service;

import com.yikolemon.pojo.User;

import java.util.List;

public interface UserService {
    User checkUser(String username, String password);

    User getUser(long id);

    User getUserByUsername(String name);

    Boolean isAdmin(String username);

    long getIdByName(String name);

    Boolean hasUserByEmail(String email);

    int saveUser(User user);

    int updateNicknameById(long id, String nickname);

    List<User> getAllUser();

}
