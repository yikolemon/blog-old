package com.yikolemon.web.admin;

import cn.hutool.core.io.IoUtil;
import com.google.gson.Gson;
import com.yikolemon.json.Img;
import com.yikolemon.util.QiniuCloudOssComponent;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RequiresRoles("admin")
@Controller
@RequestMapping("/admin")
public class FileController {

    @Resource
    QiniuCloudOssComponent qiniuOss;

    @GetMapping("/file")
    public String file(){
        return "file";
    }

    @PostMapping("/getDoc")
    @ResponseBody
    public void getDoc(MultipartFile docFile, Model model){
        String docName = docFile.getOriginalFilename();
        try {
            String docContent = IoUtil.readUtf8(docFile.getInputStream());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/getfile")
    @ResponseBody
    public String getFile(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        MultipartFile img = multipartRequest.getFile("img");
        assert img != null;
        String filename = img.getOriginalFilename();
        String src = qiniuOss.upload(img.getInputStream(), filename);
        //String src=new String("http://cdn.yikolemon.top/bg.png");
        Img imgBean=new Img(src,filename);
        Gson gson=new Gson();
        return gson.toJson(imgBean);
    }

    @PostMapping("/delete")
    @ResponseBody
    public String deleteFile(HttpServletRequest request){
        QiniuCloudOssComponent qiniuCloudOssComponent =new QiniuCloudOssComponent();
        String filename = request.getParameter("filename");
        String delete = qiniuCloudOssComponent.delete(filename);
        Gson gson=new Gson();
        Map map=new HashMap();
        map.put("msg",delete);
        String msgJson = gson.toJson(map);
        return msgJson;
    }
}
