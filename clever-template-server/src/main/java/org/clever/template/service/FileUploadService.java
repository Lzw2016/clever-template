package org.clever.template.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.validator.BaseValidatorUtils;
import org.clever.common.utils.validator.ValidatorFactoryUtils;
import org.clever.template.dto.request.UploadFileReq;
import org.clever.template.dto.response.UploadFilesRes;
import org.clever.template.entity.EnumConstant;
import org.clever.template.model.FileInfo;
import org.clever.template.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-28 11:26 <br/>
 */
@Service
@Slf4j
public class FileUploadService {

    @Autowired
    @Qualifier("LocalStorageService")
    private IStorageService storageService;

    /**
     * 解析请求 request 得到 UploadFileReq
     */
    private UploadFileReq parseUploadFileReq(HttpServletRequest request) {
        // 解析请求参数
        UploadFileReq uploadFileReq = new UploadFileReq();
        uploadFileReq.setFileSource(request.getParameter("fileSource"));
        // 是否公开可以修改(0不是，1是)
        uploadFileReq.setPublicWrite(NumberUtils.toInt(request.getParameter("publicWrite"), EnumConstant.PublicWrite_0));
        // 是否公开可以访问(0不是，1是)
        uploadFileReq.setPublicRead(NumberUtils.toInt(request.getParameter("publicRead"), EnumConstant.PublicWrite_1));
        // 目前只支持 (公开可读-私有写) 和 (私有读-私有写) 两种模式
        if (!Objects.equals(EnumConstant.PublicWrite_0, uploadFileReq.getPublicWrite())) {
            throw new BusinessException("只支持私有写(publicWrite=0)");
        }
        // 校验请求数据
        BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getHibernateValidator(), uploadFileReq);
        return uploadFileReq;
    }

    public UploadFilesRes upload(HttpServletRequest request) {
        if (!(request instanceof MultipartHttpServletRequest)) {
            throw new BusinessException("当前请求并非上传文件的请求");
        }
        // 保存上传文件
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        final long uploadStart = System.currentTimeMillis();
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        final long uploadEnd = System.currentTimeMillis();
        // 计算上传时间
        int fileCount = 0;
        for (String fileName : fileMap.keySet()) {
            MultipartFile mFile = fileMap.get(fileName);
            if (mFile.isEmpty()) {
                continue;
            }
            fileCount++;
        }
        if (fileCount <= 0) {
            throw new BusinessException("上传文件不能为空");
        }
        final long uploadTimeSum = uploadEnd - uploadStart;
        final long uploadTimeAvg = uploadTimeSum / fileCount;
        log.info("总共上传文件数量{}个,总共上传时间{}ms. 平均每个文件上传时间{}ms", fileCount, uploadTimeSum, uploadTimeAvg);
        // 解析请求参数
        UploadFileReq uploadFileReq = parseUploadFileReq(request);
        // 调用文件上传服务
        UploadFilesRes uploadFilesRes = new UploadFilesRes();
        for (String fileName : fileMap.keySet()) {
            MultipartFile mFile = fileMap.get(fileName);
            if (mFile.isEmpty()) {
                continue;
            }
            long uploadTime = uploadTimeAvg;
            if (mFile.getSize() > 0) {
                uploadTime = mFile.getSize() * uploadTimeSum / multipartRequest.getContentLengthLong();
            }
            try {
                FileInfo fileInfo = storageService.saveFile(uploadFileReq, uploadTime, mFile);
                uploadFilesRes.getSuccessList().add(fileInfo);
            } catch (Throwable e) {
                log.error("文件上传失败", e);
                uploadFilesRes.getFailList().add(FileUploadUtils.fillFileInfo(mFile));
            }
        }
        return uploadFilesRes;
    }
}
