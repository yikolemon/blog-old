package com.yikolemon.service;

import com.yikolemon.mapper.UserMapper;
import com.yikolemon.pojo.User;
import com.yikolemon.util.MD5Utils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("userServiceImpl")
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService{

    @Resource
    private UserMapper userMapper;
    @Override
    @Cacheable(key = "'checkUser'+#username+''+#password")
    public User checkUser(String username, String password) {
        return userMapper.checkByUsernameAndPassword(username, MD5Utils.code(password));
    }

    @Override
    @Cacheable(key = "'getUser'+#id")
    public User getUser(long id) {
        return userMapper.getUser(id);
    }

    @Override
    @Cacheable(key = "'getUserByUsername'+#name" , unless="#result == null")
    public User getUserByUsername(String name) {
        return userMapper.getUserByUsername(name);
    }

    @Override
    @Cacheable(key = "'isAdmin'+#username")
    public Boolean isAdmin(String username) {
        return userMapper.isAdmin(username);
    }

    @Override
    @Cacheable(key = "'getIdByName'+#name")
    public long getIdByName(String name) {
        return userMapper.getIdByName(name);
    }

    @Override
    //@Cacheable(key = "'hasUserByEmail'+#email")
    public Boolean hasUserByEmail(String email) {
        return userMapper.hasUserByEmail(email);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int saveUser(User user) {
        user.setCreateTime(new Date());
        user.setNickname(user.getUsername());
        user.setType(false);
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), null, 1024);
        user.setPassword(md5Hash.toHex());
        return userMapper.saveUser(user);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateNicknameById(long id, String nickname) {
        User user = new User();
        user.setId(id);
        user.setNickname(nickname);
        return userMapper.updateNickname(user);
    }

    @Override
    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }

}
