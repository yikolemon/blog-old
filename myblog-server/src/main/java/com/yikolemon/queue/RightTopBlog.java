package com.yikolemon.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data                              //get，set
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class RightTopBlog implements Serializable {
    //index右侧推荐最新和点击排名
    private Long id;
    private  String title;//标题
}