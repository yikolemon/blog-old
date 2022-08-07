package com.yikolemon.mapper;

import com.yikolemon.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    User checkByUsernameAndPassword(String username, String password);

    User getUser(long id);

    User getUserByUsername(String username);

    Boolean isAdmin(String username);

    long getIdByName(String name);

    Boolean hasUserByEmail(String email);

    int saveUser(User user);

    String getSaltByUsername(String username);

    int updateNickname(User user);

    List<User> getAllUser();
}
