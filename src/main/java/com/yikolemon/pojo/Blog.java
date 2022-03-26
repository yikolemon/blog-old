package com.yikolemon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data                              //get，set
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造

public class Blog implements Serializable {
    private Long id;
    private  String title;//标题
    private String description;//描述
    private  String content;//内容
    private  String firstPicture;//首图
    private  String flag;//标记(转载/原创）
    private  boolean shareStatement;//开启转载声明
    private  boolean commentabled;//开启评论
    private  boolean recommend;//推荐
    private boolean published;//是否发布
    private Date createTime;
    private Date updateTime;
    private int like;
    private int view;


    /*外键*/
    private Long typeId;
    private String typeName;
    private Long  userId;
    private String nickname;

    private String tagIds;


}
