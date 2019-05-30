package org.clever.template.servlet;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 自定义的处理文件上传的解析器(限制上传速度)<br>
 * 作者：LiZW <br/>
 * 创建时间：2016/11/17 21:21 <br/>
 *
 * @see CommonsMultipartResolver
 */
@SuppressWarnings("NullableProblems")
@Slf4j
public class SpeedLimitMultipartResolver extends CommonsMultipartResolver {

    /**
     * 上传限速配置
     */
    private final long speedLimit;

    public SpeedLimitMultipartResolver(long speedLimit) {
        super();
        this.speedLimit = speedLimit;
    }

    /**
     * 自定义创建DiskFileItemFactory
     */
    @Override
    protected DiskFileItemFactory newFileItemFactory() {
        // diskFileItemFactory.setFileCleaningTracker(pTracker);
        return super.newFileItemFactory();
    }

    /**
     * 解析上传的文件，此方法必定会被执行
     * 重写此方法的主要目的是限制上传速度，其他代码参考父类实现
     */
    @Override
    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = determineEncoding(request);
        FileUpload fileUpload = prepareFileUpload(encoding);
        // 注册文件上传时的上传进度监听器，用于限制上传速度
        SpeedLimitProgressListener speedLimitProgressListener;
        if (speedLimit > 0) {
            speedLimitProgressListener = new SpeedLimitProgressListener(speedLimit);
        } else {
            speedLimitProgressListener = new SpeedLimitProgressListener();
        }
        fileUpload.setProgressListener(speedLimitProgressListener);
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            MultipartParsingResult multipartParsingResult = parseFileItems(fileItems, encoding);
            // 删除Form表单数据对应的临时文件
            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()) {
                    try {
                        fileItem.delete();
                    } catch (Throwable e) {
                        log.warn("无法删除临时文件:[{}]", fileItem.getFieldName(), e);
                    }
                    log.info("Cleaning up form part, fieldName '{}'", fileItem.getFieldName());
                }
            }
            return multipartParsingResult;
        } catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        } catch (FileUploadBase.FileSizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getFileSizeMax(), ex);
        } catch (FileUploadException ex) {
            throw new MultipartException("Failed to parse multipart servlet request", ex);
        }
    }
}
