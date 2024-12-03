package com.yikolemon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data                              //get，set
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class User implements Serializable {
    private Long id;
    private String nickname;
    private String username;
    private String password;
    @Email(message = "邮箱格式不正确")
    private String email;
    private String avatar;
    private Boolean type;
    private Date createTime;
    private Date updateTime;

}
