package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author: Joey
 * @Description:
 * @date:2024/5/24 15:34
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用文件操作")
@Slf4j
public class CommonController {


    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    //请求中要携带上需要上传的文件
    public Result<String> saveOss(MultipartFile file) {
        try {
            //获取原始的文件名
            String originalFilename = file.getOriginalFilename();
            //上传文件重命名 在oss中存储名字就是UUID + 文件的后缀名
            String objectName = UUID.randomUUID()+originalFilename.substring(originalFilename.lastIndexOf("."));
            String resultURL = aliOssUtil.upload(file.getBytes(), objectName);
            log.info("返回上传文件url: "+resultURL);
            return Result.success(resultURL);
        } catch (IOException e) {
            log.info("上传文件失败");
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/check")
    public Result<String> statusCheck(){
        return Result.success();
    }
}
