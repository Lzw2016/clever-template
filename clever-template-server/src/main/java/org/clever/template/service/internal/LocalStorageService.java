package org.clever.template.service.internal;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.IPAddressUtils;
import org.clever.template.config.GlobalConfig;
import org.clever.template.dto.request.UploadFileReq;
import org.clever.template.entity.EnumConstant;
import org.clever.template.model.FileInfo;
import org.clever.template.utils.StoragePathUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

/**
 * 上传文件存储到当前服务器的Service<br>
 * <p>
 * 作者：LiZW <br/>
 * 创建时间：2016/11/17 22:17 <br/>
 */
@Service("LocalStorageService")
@Slf4j
public class LocalStorageService extends AbstractStorageService {

    private static final String publicReadBasePath = File.separator + "public-read";

    /**
     * 上传文件存储到当前服务器的路径，如：F:\fileStoragePath<br>
     * <p>
     * <b>注意：路径后面没有多余的“\”或者“/”</b>
     */
    private final String diskBasePath;
    /**
     * 文件存储节点, 只支持本机IP
     */
    private final String storedNode;

    public LocalStorageService(GlobalConfig globalConfig) {
        diskBasePath = globalConfig.getLocalStorageConfig().getDiskBasePath();
        if (StringUtils.isBlank(diskBasePath)) {
            throw new IllegalArgumentException("[本地服务器]文件上传到本地硬盘的基础路径(diskBasePath)未配置");
        }
        File file = new File(diskBasePath);
        if (file.exists() && file.isFile()) {
            throw new IllegalArgumentException("[本地服务器]文件上传到本地硬盘的基础路径(diskBasePath)=[" + diskBasePath + "]不能是文件");
        }
        if (!file.exists() && file.mkdirs()) {
            log.info("[本地服务器]创建文件夹：" + diskBasePath);
        }

        storedNode = globalConfig.getLocalStorageConfig().getStoredNode();
//        TODO 127.0.0.1
//        if ("127.0.0.1".equals(storedNode)) {
//            throw new IllegalArgumentException("[本地服务器]文件存储节点(storedNode)=[" + storedNode + "]不能是127.0.0.1");
//        }
        Set<String> ipAddress = IPAddressUtils.getInet4Address();
        if (!ipAddress.contains(storedNode)) {
            ipAddress.remove("127.0.0.1");
            throw new IllegalArgumentException("[本地服务器]文件存储节点(storedNode)=[" + storedNode + "]可选值：" + ipAddress.toString());
        }
    }

    @Override
    protected String getStoredNode() {
        return storedNode;
    }

    @Override
    protected Integer getStoredType() {
        return EnumConstant.StoredType_1;
    }

    // 上传文件
    @Override
    protected void internalSaveFile(FileInfo fileInfo, UploadFileReq uploadFileReq, long uploadTime, MultipartFile multipartFile) throws IOException {
        // 上传文件存储到当前服务器的路径(相对路径，相对于 FILE_STORAGE_PATH)
        String filePath = StoragePathUtils.generateFilePathByDate(publicReadBasePath, File.separator);
        fileInfo.setFilePath(filePath);
        // 计算文件的绝对路径，保存文件
        String absoluteFilePath = diskBasePath + filePath + File.separator + fileInfo.getNewName();
        File file = new File(absoluteFilePath);
        long storageStart = System.currentTimeMillis();
        // 文件夹不存在，创建文件夹
        File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            if (parentFile.mkdirs()) {
                log.info("创建文件夹：" + parentFile.getPath());
            } else {
                throw new RuntimeException("创建文件夹[" + parentFile.getPath() + "]失败");
            }
        }
        // 如果filePath表示的不是一个路径，文件就会被存到System.getProperty("user.dir")路径下
        multipartFile.transferTo(file);
        long storageEnd = System.currentTimeMillis();
        // 设置存储所用的时间
        fileInfo.setStoredTime(storageEnd - storageStart);
        log.info("[本地服务器]文件存储所用时间:[{}ms]", fileInfo.getStoredTime());
        fileInfo.setReadUrl(fileInfo.getFilePath() + File.separator + fileInfo.getNewName());
    }

    @Override
    public boolean isExists(FileInfo fileInfo) {
        if (fileInfo == null) {
            return false;
        }
        String fullPath = diskBasePath + fileInfo.getFilePath();
        fullPath = FilenameUtils.concat(fullPath, fileInfo.getNewName());
        File file = new File(fullPath);
        if (file.exists() && file.isFile()) {
            return true;
        }
        log.warn("[本地服务器]文件引用[NewName={}]对应的文件不存在", fileInfo.getNewName());
        return false;
    }

    // 限速打开文件
    @SuppressWarnings("UnstableApiUsage")
    @Override
    protected void internalOpenFileSpeedLimit(FileInfo fileInfo, OutputStream outputStream, long off, long len, long maxSpeed) throws IOException {
        RateLimiter rateLimiter = null;
        if (maxSpeed > 0) {
            rateLimiter = RateLimiter.create(maxSpeed);
        }
        String fullPath = diskBasePath + fileInfo.getFilePath();
        fullPath = FilenameUtils.concat(fullPath, fileInfo.getNewName());
        File file = new File(fullPath);
        try (InputStream inputStream = FileUtils.openInputStream(file)) {
            if (off > 0) {
                long tmp = inputStream.skip(off);
                if (tmp != off) {
                    throw new BusinessException("off参数错误", 416);
                }
            }
            byte[] data = new byte[8 * 1024];
            int readByte;
            long writeSize = 0;
            double sleepTime = 0;
            while (true) {
                readByte = inputStream.read(data);
                if (readByte == 0) {
                    continue;
                }
                if (readByte < 0) {
                    break;
                }
                writeSize += readByte;
                if (len > 0 && writeSize >= len) {
                    outputStream.write(data, 0, (int) (len - (writeSize - readByte)));
                    break;
                } else {
                    outputStream.write(data, 0, readByte);
                }
                if (rateLimiter != null) {
                    sleepTime = rateLimiter.acquire(readByte);
                }
                log.debug("[本地服务器]打开文件NewName:[{}], 读取字节数:[{}], 休眠时间:[{}]秒", fileInfo.getNewName(), readByte, sleepTime);
            }
            outputStream.flush();
        }
    }
}

