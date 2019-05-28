package org.clever.template.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.clever.template.dto.response.UploadFilesRes;
import org.clever.template.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-28 11:19 <br/>
 */
@Api("文件上传")
@RequestMapping("/api/file")
@RestController
@Slf4j
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation("文件上传，支持多文件上传")
    @PostMapping("/upload")
    public UploadFilesRes upload(HttpServletRequest request) {
        return fileUploadService.upload(request);
    }

//    @ApiOperation("根据文件newName打开文件")
//    @GetMapping("/open_speed_limit/{newName}")
//    public void openFileSpeedLimit(HttpServletRequest request, HttpServletResponse response, @PathVariable String newName) {
//        fileUploadService.openFile(true, request, response, newName);
//    }
}
