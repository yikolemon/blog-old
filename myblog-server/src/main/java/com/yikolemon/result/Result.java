package com.yikolemon.result;

import com.google.gson.Gson;

public class Result {
    private boolean res;
    private String msg;


    public Result(boolean res, String msg) {
        this.res = res;
        this.msg = msg;
    }

    public static String suc(){
        return new Gson().toJson(new Result(true,null));
    }

    public static String fall(String msg){
        return new Gson().toJson(new Result(false,msg));
    }
}
