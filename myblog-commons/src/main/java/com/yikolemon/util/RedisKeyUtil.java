package com.yikolemon.util;


public class RedisKeyUtil {

    private static final String SPLIT=":";
    private static final String PREFIX_UV="uv";
    private static final String PREFIX_DAU="dau";
    private static final String SearchLimit="searchLimit";
    private static final String redisLock="redisLock";
    private static final String unreceiveMsgList="unreceiveMsgList";
    private static final String unreceiveFromSet="unreceiveFromSet";
    private static final String cloudPublicSaveKey="cloudPublicSaveKey";
    private static final String cloudP2PSaveKey="cloudP2PSaveKey";


    /*单日uv*/
    public static String getUVKey(String data){
        return PREFIX_UV + SPLIT + data;
    }

    /*区间uv*/
    public static String getUVKey(String startData,String endData){
        return PREFIX_UV+ SPLIT + startData + SPLIT + endData;
    }

    /*单日活跃用户*/
    public static String getDAUKey(String data){
        return PREFIX_DAU + SPLIT + data;
    }

    /*区间活跃用户*/
    public static String getDAUKey(String startData,String endData){
        return PREFIX_DAU + SPLIT +startData + SPLIT + endData;
    }

    /*搜索频率限制key*/
    public static String getSearchLimitKey(String principal){
        return SearchLimit+SPLIT+principal;
    }

    /*redis锁*/
    public static String getRedisLock(String username){
        return redisLock+SPLIT+username;
    }

    /*redis未存储的key*/
    public static String getUnreceiveMsgListKey(String username,String from){
        return unreceiveMsgList+SPLIT+username+SPLIT+from;
    }

    /*未存储key的set集合*/
    public static String getUnreceiveFromSetKey(String username){
        return unreceiveFromSet+SPLIT+username;
    }

    public static String getCloudP2PMsgSaveKey(String k1,String k2){
        int i = k1.compareTo(k2);
        if (i>0){
            String temp=k1;
            k1=k2;
            k2=temp;
        }
        return cloudP2PSaveKey+SPLIT+k1+SPLIT+k2;
    }

    public static String getCloudPublicMsgSaveKey(){
        return cloudPublicSaveKey;
    }

}
