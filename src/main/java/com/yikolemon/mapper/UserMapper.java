package com.yikolemon.mapper;

import com.yikolemon.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    User checkByUsernameAndPassword(String username,String password);

    User getUser(long id);
}
