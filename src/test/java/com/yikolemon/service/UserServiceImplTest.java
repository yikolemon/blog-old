package com.yikolemon.service;

import com.yikolemon.mapper.UserMapper;
import com.yikolemon.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
    @Test
    public void checkUser() {
        User user = userService.checkUser("yikolemon", "123456");
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
    }

    @Test
    public void getUser(){
        User user = userService.getUser(1);
        System.out.println(user);
    }
}