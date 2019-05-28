package org.clever.template.config;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.exception.ExceptionUtils;
import org.clever.template.servlet.SpeedLimitMultipartResolver;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.IOException;

/**
 * 作者： lzw<br/>
 * 创建时间：2017-12-04 10:37 <br/>
 */
@Configuration
@Slf4j
public class BeanConfiguration {

    @Bean("multipartResolver")
    protected CommonsMultipartResolver commonsMultipartResolver(GlobalConfig globalConfig, MultipartProperties multipartProperties) {
        MultipartConfigElement multipartConfigElement = multipartProperties.createMultipartConfig();
        SpeedLimitMultipartResolver speedLimitMultipartResolver = new SpeedLimitMultipartResolver(globalConfig.getUploadSpeedLimit());
        speedLimitMultipartResolver.setDefaultEncoding("UTF-8");
        speedLimitMultipartResolver.setMaxInMemorySize(multipartConfigElement.getFileSizeThreshold());
        speedLimitMultipartResolver.setMaxUploadSize(multipartConfigElement.getMaxRequestSize());
        speedLimitMultipartResolver.setMaxUploadSizePerFile(multipartConfigElement.getMaxFileSize());
        speedLimitMultipartResolver.setPreserveFilename(false);
        speedLimitMultipartResolver.setResolveLazily(true);
        try {
            File file = new File(multipartConfigElement.getLocation());
            if (file.exists() && file.isFile()) {
                throw new IllegalArgumentException("文件上传临时路径=[" + multipartConfigElement.getLocation() + "]不能是文件");
            }
            if (!file.exists() && file.mkdirs()) {
                log.info("[本地服务器]创建文件夹：" + multipartConfigElement.getLocation());
            }
            speedLimitMultipartResolver.setUploadTempDir(new FileSystemResource(multipartConfigElement.getLocation()));
        } catch (IOException e) {
            throw ExceptionUtils.unchecked(e);
        }
        return speedLimitMultipartResolver;
    }
}
