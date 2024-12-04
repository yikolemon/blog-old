package com.yikolemon.service;

import com.yikolemon.pojo.DAUData;
import com.yikolemon.pojo.UVData;

import java.util.Date;
import java.util.List;

public interface DataService {

    // 记录唯一访问者（UV）
    void recordUV(String ip);

    // 记录日活跃用户（DAU）
    void recordDAU(int userId);

    // 计算过去七天的 UV
    long lastSevenDayUv();

    // 计算过去七天的 DAU
    long lastSevenDayDau();

    // 计算指定日期范围的 UV
    long calculateUv(Date start, Date end);

    // 计算指定日期范围的 DAU
    long calculateDAU(Date start, Date end);

    // 加载数据从数据库到内存（用于重启恢复）
    void loadDataFromDatabase();

    // 同步内存数据到数据库
    void syncDataToDatabase();

    // 清理过期数据（保持最近 8 天的数据）
    void cleanupExpiredData();

    // 关闭定时任务
    void shutdown();

    // 获取所有 UV 数据
    List<UVData> getAllUVDataFromDb();

    // 获取所有 DAU 数据
    List<DAUData> getAllDAUDataFromDb();
}
