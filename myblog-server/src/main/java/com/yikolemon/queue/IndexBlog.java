package com.yikolemon.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data                              //get，set
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class IndexBlog implements Serializable {

    private Long id;
    private  String title;//标题
    private String description;//描述
    private  String firstPicture;//首图
    private  Integer view;//浏览次数
    private Date updateTime;

    /*外键*/
    private Long typeId;
    private String typeName;
    private String nickname;
    private String avatar;
}
