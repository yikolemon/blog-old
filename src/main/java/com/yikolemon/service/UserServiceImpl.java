package com.yikolemon.service;

import com.yikolemon.mapper.UserMapper;
import com.yikolemon.pojo.User;
import com.yikolemon.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;
    @Override
    public User checkUser(String username, String password) {
        User user=userMapper.checkByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }

    @Override
    public User getUser(long id) {
        return userMapper.getUser(id);
    }

    @Override
    public User getUserByUsername(String name) {
        return userMapper.getUserByUsername(name);
    }

    @Override
    public Boolean isAdmin(String username) {
        return userMapper.isAdmin(username);
    }

    @Override
    public long getIdByName(String name) {
        return userMapper.getIdByName(name);
    }
}
