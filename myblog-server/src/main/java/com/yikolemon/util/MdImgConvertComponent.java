package com.yikolemon.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.File;

/**
 * @author duanfuqiang
 * @date 2024/12/4
 **/
@Component
public class MdImgConvertComponent {

    @Resource
    private QiniuCloudOssComponent qiniuOss;

    public String replaceImagesWithOssPaths(String content) {
        // 匹配 ![alt](image_url) 格式的正则表达式
        String regex = "!\\[(.*?)\\]\\((.*?)\\)";
        return ReUtil.replaceAll(content, regex, (matcher) -> {
            String imageUrl = matcher.group(2); // 获取图片的URL

            // 下载图片
            String filePath =  "./blog-pic/" + FileUtil.getName(imageUrl);
            File localFile = FileUtil.file(filePath);
            HttpUtil.downloadFile(imageUrl, localFile);
            String absolutePath = localFile.getAbsolutePath();
            String ossPath = qiniuOss.upload(absolutePath, FileUtil.getName(imageUrl));
            // 返回新的Markdown语法，替换图片的URL为oss路径
            return "![" + matcher.group(1) + "](" + ossPath + ")";
        });
    }
}
