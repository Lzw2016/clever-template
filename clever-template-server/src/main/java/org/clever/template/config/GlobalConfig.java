package org.clever.template.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2017-12-04 12:44 <br/>
 */
@Component
@ConfigurationProperties(prefix = "clever.config")
@Data
public class GlobalConfig {

    /**
     * 上传限速配置(?byte/s)
     */
    private long uploadSpeedLimit = 1024 * 1024;

    /**
     * 下载限速配置(?byte/s)
     */
    private long downloadSpeedLimit = 1024 * 1024;

    /**
     * 文件上传到本地硬盘的基础路径
     */
    @NestedConfigurationProperty
    private LocalStorageConfig localStorageConfig;

    @Data
    public static class LocalStorageConfig implements Serializable {
        private String diskBasePath;
        /**
         * 文件存储节点, 只支持本机IP
         */
        private String storedNode;
    }
}
