package com.yikolemon.util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Component
public class QiniuCloudOssComponent {
    // 设置需要操作的账号的AK和SK
    @Value("${qiniu.access_key}")
    private String ACCESS_KEY;

    @Value("${qiniu.secret_key}")
    private String SECRET_KEY;

    // 要上传的空间
    @Value("${qiniu.bucketname}")
    private String BUCKETNAME;

    // 密钥
    private Auth auth = null;

    @PostConstruct
    public void init(){
        auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    }

    @Value("${qiniu.domain}")
    private String DOMAIN;

    private static final String HTTP_HEAD = "http://";

    private static final String style = "自定义的图片样式";

    //配置七牛云的空间区域
    private static final Configuration con=new Configuration(Zone.huadong());

    public String getUpToken() {
        return auth.uploadToken(BUCKETNAME, null, 3600, new StringMap().put("insertOnly", 1));
    }

    // 普通上传
    public String upload(String filePath, String fileName) throws IOException {
        // 创建上传对象
        Configuration configuration=new Configuration(Zone.huadong());
        UploadManager uploadManager = new UploadManager(configuration);

        try {
            // 调用put方法上传
            String token = auth.uploadToken(BUCKETNAME);
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
                return HTTP_HEAD + DOMAIN + ret.key;
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
            String token = auth.uploadToken(BUCKETNAME);
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
                return HTTP_HEAD + DOMAIN + ret.key;
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
        try {
            Response delete = bucketMgr.delete(BUCKETNAME, fileName);
            delete.close();
        }catch (Exception e){
            e.printStackTrace();
            return "删除失败";
        }
        return "删除成功";
    }

    static class Ret {
        public long fsize;
        public String key;
        public String hash;
        public int width;
        public int height;
    }

}

