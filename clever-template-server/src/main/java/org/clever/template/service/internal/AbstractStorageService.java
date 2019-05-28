package org.clever.template.service.internal;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.clever.common.exception.BusinessException;
import org.clever.template.dto.request.UploadFileReq;
import org.clever.template.entity.EnumConstant;
import org.clever.template.model.FileInfo;
import org.clever.template.service.IStorageService;
import org.clever.template.utils.FileDigestUtils;
import org.clever.template.utils.FileUploadUtils;
import org.clever.template.utils.StoragePathUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-12-27 12:02 <br/>
 */
@Transactional(readOnly = true)
@Slf4j
public abstract class AbstractStorageService implements IStorageService {

    protected abstract String getStoredNode();

    protected abstract Integer getStoredType();

    @Transactional(propagation = Propagation.NEVER)
    @Override
    public FileInfo saveFile(UploadFileReq uploadFileReq, long uploadTime, MultipartFile multipartFile) throws IOException {
        // 设置文件签名类型 和 文件签名
        String digest;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            digest = FileDigestUtils.FileDigestByMD5(inputStream);
        }
        // 服务器端不存在相同文件
        FileInfo fileInfo = new FileInfo();
        fileInfo = FileUploadUtils.fillFileInfo(fileInfo, multipartFile);
        fileInfo.setFileSource(uploadFileReq.getFileSource());
        fileInfo.setUploadTime(uploadTime);
        fileInfo.setDigest(digest);
        fileInfo.setDigestType(EnumConstant.DigestType_1);
        // 上传文件的存储类型：阿里云OSS
        // 设置文件存储之后的名称：UUID + 后缀名(此操作依赖文件原名称)
        String newName = StoragePathUtils.generateNewFileName(fileInfo.getFileName());
        fileInfo.setNewName(newName);
        fileInfo.setFileSuffix(FilenameUtils.getExtension(fileInfo.getFileName()).toLowerCase());
        // 调用内部保存文件方法
        internalSaveFile(fileInfo, uploadFileReq, uploadTime, multipartFile);
        return fileInfo;
    }

    /**
     * 存储文件之后,回写以下字段 <br />
     * fileInfo.setFilePath(filePath); <br />
     * fileInfo.setStoredTime(storageEnd - storageStart); <br />
     * fileInfo.setReadUrl(""); <br />
     */
    protected abstract void internalSaveFile(FileInfo fileInfo, UploadFileReq uploadFileReq, long uploadTime, MultipartFile multipartFile) throws IOException;

//    @Override
//    public FileInfo getFileInfo(String newName) {
//        FileInfo fileInfo = getFileInfoMapper().getByNewName(newName, getStoredType());
//        return isExists(fileInfo) ? fileInfo : null;
//    }
//
//    @Override
//    public FileInfo getFileInfo(Long fileId) {
//        FileInfo fileInfo = getFileInfoMapper().selectById(fileId);
//        return isExists(fileInfo) ? fileInfo : null;
//    }

    @Transactional(propagation = Propagation.NEVER)
    @Override
    public void openFileSpeedLimit(FileInfo fileInfo, OutputStream outputStream, long off, long len, long maxSpeed) throws IOException {
        if (fileInfo == null) {
            throw new IllegalArgumentException("文件信息不能为空");
        }
        if (!isExists(fileInfo)) {
            throw new BusinessException("文件不存在", 404);
        }
        internalOpenFileSpeedLimit(fileInfo, outputStream, off, len, maxSpeed);
    }

    /**
     * 限速打开文件
     */
    protected abstract void internalOpenFileSpeedLimit(FileInfo fileInfo, OutputStream outputStream, long off, long len, long maxSpeed) throws IOException;
}
