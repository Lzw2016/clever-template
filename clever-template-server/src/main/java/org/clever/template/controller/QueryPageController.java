package org.clever.template.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.clever.template.dto.request.QueryPermissionReq;
import org.clever.template.entity.Permission;
import org.clever.template.service.QueryPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-06-13 16:50 <br/>
 */
@Api(description = "分页查询")
@RequestMapping("/api/query_page")
@RestController
@Slf4j
public class QueryPageController {

    @Autowired
    private QueryPageService queryPageService;

    @GetMapping("/find")
    public IPage<Permission> findPermission() {
        return queryPageService.findPermission();
    }

    @GetMapping("/find2")
    public IPage<Permission> findPermission(QueryPermissionReq query) {
        return queryPageService.findPermission(query);
    }

    @GetMapping("detail_data")
    public Object getDetailData() throws InterruptedException {
        Thread.sleep(1000 * (int) (Math.random() * 5));
        Map<String, Object> data = new HashMap<>();
        data.put("name", "freemenL");
        data.put("dutyType", "早班");
        data.put("deptName", "技术研发部 xxx部门 xxx部门 xxx部门");
        data.put("startTime", "9:00");
        data.put("endTime", "6:00");
        data.put("onRange", "9:00-9:30");
        data.put("offRange", "6:00-11:59");
        data.put("duration", "1小时");
        data.put("stats", 1);
        return data;
    }
}
