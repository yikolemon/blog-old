package com.yikolemon.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data                              //get，set
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class IndexTag implements Serializable {
    private Long id;

    private String name;

    private int num;//排序后得到的标签含有博客的数量
}