package org.clever.template.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.template.dto.response.RemoteDataRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-21 10:43 <br/>
 */
@Api("远程数据搜索")
@RequestMapping("/api/remote/input")
@RestController
@Slf4j
public class RemoteSelectController {

    @GetMapping("/string")
    public List<String> getString(
            @RequestParam(value = "key", required = false, defaultValue = "") String key,
            @RequestParam(value = "num", required = false, defaultValue = "10") int num) throws InterruptedException {
        Thread.sleep(1000 * (int) (Math.random() * 5));
        List<String> result = new ArrayList<>(num);
        if (Objects.equals("test", key)) {
            return result;
        }
        if (Objects.equals("test1", key)) {
            throw new BusinessException("测试异常");
        }
        for (int i = 1; i <= num; i++) {
            result.add(key + "-数据" + i);
        }
        return result;
    }

    @GetMapping("/object")
    public List<RemoteDataRes> getObject(
            @RequestParam(value = "key", required = false, defaultValue = "") String key,
            @RequestParam(value = "num", required = false, defaultValue = "10") int num) throws InterruptedException {
        Thread.sleep(1000 * (int) (Math.random() * 5));
        List<RemoteDataRes> result = new ArrayList<>(num);
        if (Objects.equals("test", key)) {
            return result;
        }
        if (Objects.equals("test1", key)) {
            throw new BusinessException("测试异常");
        }
        for (int i = 1; i <= num; i++) {
            result.add(new RemoteDataRes(key + "-数据" + i));
        }
        return result;
    }
}
