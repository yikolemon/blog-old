package com.yikolemon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UVData {
    
    private String dateKey;  // 日期（格式：yyyyMMdd）
    private String ip;       // 访问者 IP

}
