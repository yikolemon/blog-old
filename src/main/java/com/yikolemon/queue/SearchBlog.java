package com.yikolemon.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class SearchBlog implements Serializable {
    private  String title;//标题
    private Long typeId;
    private  boolean recommend;//推荐
    private boolean published; //发布
}
