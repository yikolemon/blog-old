package com.yikolemon.mapper;

import com.yikolemon.pojo.DAUData;
import com.yikolemon.pojo.UVData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DataMapper {

    @Select("SELECT * FROM uv_data")
    List<UVData> getAllUVData();

    // 获取所有 DAU 数据
    @Select("SELECT * FROM dau_data")
    List<DAUData> getAllDAUData();

    // 保存UV数据
    @Insert("INSERT OR IGNORE INTO uv_data (date, ip) VALUES (#{date}, #{ip})")
    void insertUVData(@Param("date") String date, @Param("ip") String ip);

    // 保存DAU数据
    @Insert("INSERT OR IGNORE INTO dau_data (date, user_id) VALUES (#{date}, #{userId})")
    void insertDAUData(@Param("date") String date, @Param("userId") int userId);

    // 获取某个日期的UV
    @Select("SELECT COUNT(DISTINCT ip) FROM uv_data WHERE date = #{date}")
    long getUVCount(@Param("date") String date);

    // 获取某个日期的DAU
    @Select("SELECT COUNT(DISTINCT user_id) FROM dau_data WHERE date = #{date}")
    long getDAUCount(@Param("date") String date);

    // 删除过期的UV数据
    @Delete("DELETE FROM uv_data WHERE date < #{expiryDate}")
    void deleteExpiredUVData(@Param("expiryDate") String expiryDate);

    // 删除过期的DAU数据
    @Delete("DELETE FROM dau_data WHERE date < #{expiryDate}")
    void deleteExpiredDAUData(@Param("expiryDate") String expiryDate);
}
