package com.yikolemon.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data                              //get，set
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class ArchiveBlog implements Serializable {

    private Long id;
    private  String title;//标题
    private String flag;//区别原创转载
    private String date;//月份和日

}
