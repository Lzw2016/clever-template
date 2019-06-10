package org.clever.template.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-21 10:43 <br/>
 */
@Api("表单数据提交")
@RequestMapping("/api/remote/form")
@RestController
@Slf4j
public class FormEngineController {

    @PostMapping("/submit")
    public Object submit(@RequestBody String body) {
        List<String> result = new ArrayList<>();
        log.info("body --> {}", body);
        result.add(body);
        return result;
    }
}
