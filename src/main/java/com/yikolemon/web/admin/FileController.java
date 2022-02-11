package com.yikolemon.web.admin;

import com.google.gson.Gson;
import com.yikolemon.json.Img;
import com.yikolemon.util.QiniuCloudUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class FileController {

    @GetMapping("/file")
    public String file(){
        return "file";
    }

    @PostMapping("/getfile")
    @ResponseBody
    public String getFile(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        MultipartFile img = multipartRequest.getFile("img");
        String filename = img.getOriginalFilename();
        QiniuCloudUtil qiniu=new QiniuCloudUtil();
        String src = "http://"+qiniu.upload(img.getInputStream(), filename);
        //String src=new String("http://cdn.yikolemon.top/bg.png");
        Img imgBean=new Img(src,filename);
        Gson gson=new Gson();
        String imgJson = gson.toJson(imgBean);
        System.out.println(imgJson);
        return imgJson;
    }

    @PostMapping("/delete")
    @ResponseBody
    public String deleteFile(HttpServletRequest request){
        QiniuCloudUtil qiniuCloudUtil=new QiniuCloudUtil();
        String filename = request.getParameter("filename");
        String delete = qiniuCloudUtil.delete(filename);
        Gson gson=new Gson();
        Map map=new HashMap();
        map.put("msg",delete);
        String msgJson = gson.toJson(map);
        return msgJson;
    }
}
