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
}
