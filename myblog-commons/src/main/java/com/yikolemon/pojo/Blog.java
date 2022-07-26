package com.yikolemon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Data                              //get，set
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造

@Document(indexName = "blog",type = "_doc",shards = 3,replicas = 0)
public class Blog implements Serializable {

    @Id
    private Long id;

    @Field(type = FieldType.Text ,analyzer = "ik_max-word" ,searchAnalyzer = "ik_smart")
    private  String title;//标题

    @Field(type = FieldType.Text ,analyzer = "ik_max-word" ,searchAnalyzer = "ik_smart")
    private String description;//描述

    @Field(type = FieldType.Text ,analyzer = "ik_max-word" ,searchAnalyzer = "ik_smart")
    private  String content;//内容

    @Field(type = FieldType.Text)
    private  String firstPicture;//首图

    @Field(type = FieldType.Text)
    private  String flag;//标记(转载/原创）

    @Field(type = FieldType.Boolean)
    private  boolean shareStatement;//开启转载声明

    @Field(type = FieldType.Boolean)
    private  boolean commentabled;//开启评论

    @Field(type = FieldType.Boolean)
    private  boolean recommend;//推荐

    @Field(type = FieldType.Boolean)
    private boolean published;//是否发布

    @Field(type = FieldType.Date ,format = DateFormat.basic_date_time)
    private Date createTime;

    @Field(type = FieldType.Date ,format = DateFormat.basic_date_time)
    private Date updateTime;
    @Field(type =FieldType.Integer)
    private int view;


    /*外键*/
    private Long typeId;
    private String typeName;
    private Long  userId;
    private String nickname;

    private String tagIds;


}
