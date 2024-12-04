package com.yikolemon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DAUData {

    private String dateKey;  // 日期（格式：yyyyMMdd）
    private int userId;      // 用户 ID

}
