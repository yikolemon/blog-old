package com.yikolemon.util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;


import java.io.IOException;
import java.io.InputStream;

public class QiniuCloudUtil {
    // 设置需要操作的账号的AK和SK
    private static final String ACCESS_KEY = "XKB8qnTudEjAUUAHk01-p44QV5mGc6b2VsxrKL2o";
    private static final String SECRET_KEY = "Or1Gkzxgi0jq-ZnjA2T3NKtOmYTtIZ9SIaZwaL00";

    // 要上传的空间
    private static final String bucketname = "yikolemon-blog";

    // 密钥
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    private static final String DOMAIN = "cdn.yikolemon.cn/";

    private static final String style = "自定义的图片样式";

    //配置七牛云的空间区域
    private static final Configuration con=new Configuration(Zone.huadong());

    public String getUpToken() {
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 1));
    }

    // 普通上传
    public String upload(String filePath, String fileName) throws IOException {
        // 创建上传对象
        Configuration configuration=new Configuration(Zone.huadong());
        UploadManager uploadManager = new UploadManager(configuration);

        try {
            // 调用put方法上传
            String token = auth.uploadToken(bucketname);
            //判断字符串为空的工具类，可用自己的
            if (StrUtil.isEmpty(token)) {
                System.out.println("未获取到token，请重试！");
                return null;
            }
            Response res = uploadManager.put(filePath, fileName, token);
            // 打印返回的信息
            System.out.println(res.bodyString());
            if (res.isOK()) {
                Ret ret = res.jsonToObject(Ret.class);
                //如果不需要对图片进行样式处理，则使用以下方式即可
                return DOMAIN + ret.key;
                //return DOMAIN + ret.key + "?" + style;
            }
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                // 响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                // ignore
            }
        }
        return null;
    }


    public String upload(InputStream in,String fileName) throws IOException{
        //Configuration configuration=new Configuration(Zone.zoneAs0());
        UploadManager uploadManager = new UploadManager(con);
        try {
            // 调用put方法上传
            String token = auth.uploadToken(bucketname);
            //判断字符串为空的工具类，可用自己的
            if (StrUtil.isEmpty(token)) {
                System.out.println("未获取到token，请重试！");
                return null;
            }

            Response res = uploadManager.put(in,fileName,token,null,null);
            // 打印返回的信息
            System.out.println(res.bodyString());
            if (res.isOK()) {
                Ret ret = res.jsonToObject(Ret.class);
                //如果不需要对图片进行样式处理，则使用以下方式即可
                return DOMAIN + ret.key;
                //return DOMAIN + ret.key + "?" + style;
            }
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                // 响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                // ignore
            }
        }
        return null;
    }

    public String delete(String fileName){
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        //Configuration config = new Configuration(Zone.zoneAs0());
        BucketManager bucketMgr = new BucketManager(auth, con);
        //指定需要删除的文件，和文件所在的存储空间
        String bucketName = bucketname;
        String  key = fileName;
        try {
            Response delete = bucketMgr.delete(bucketName, key);
            delete.close();
        }catch (Exception e){
            e.printStackTrace();
            return "删除失败";
        }
        return "删除成功";
    }

    class Ret {
        public long fsize;
        public String key;
        public String hash;
        public int width;
        public int height;
    }

}

