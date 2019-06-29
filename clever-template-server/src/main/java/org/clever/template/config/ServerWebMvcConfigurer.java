package org.clever.template.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-17 14:29 <br/>
 */
@Configuration
public class ServerWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private GlobalConfig globalConfig;

    /**
     * 自定义静态资源访问映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/excel-templates/**").addResourceLocations("classpath:/excel-templates/");
        File file = new File(globalConfig.getLocalStorageConfig().getDiskBasePath());
        registry.addResourceHandler("/api/file/**").addResourceLocations("file:///" + file.getAbsolutePath() + File.separator);
    }
}
