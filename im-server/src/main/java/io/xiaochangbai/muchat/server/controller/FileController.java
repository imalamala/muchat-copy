package io.xiaochangbai.muchat.server.controller;

import io.xiaochangbai.muchat.common.core.utils.Result;
import io.xiaochangbai.muchat.common.core.utils.ResultUtils;
import io.xiaochangbai.muchat.server.service.thirdparty.FileService;
import io.xiaochangbai.muchat.server.vo.UploadImageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@Api(tags = "文件上传")
public class FileController {


    @Autowired
    private FileService fileService;

    @ApiOperation(value = "上传图片",notes="上传图片,上传后返回原图和缩略图的url")
    @PostMapping("/image/upload")
    public Result<UploadImageVO> uploadImage(MultipartFile file) {
        return ResultUtils.success(fileService.uploadImage(file));
    }

    @ApiOperation(value = "上传文件",notes="上传文件，上传后返回文件url")
    @PostMapping("/file/upload")
    public Result<String> uploadFile(MultipartFile file) {
        return ResultUtils.success(fileService.uploadFile(file),"");
    }

}