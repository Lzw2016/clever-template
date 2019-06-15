package org.clever.template.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.clever.template.dto.request.QueryPermissionReq;
import org.clever.template.entity.Permission;
import org.clever.template.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-06-13 18:42 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class QueryPageService {

    @Autowired
    private PermissionMapper permissionMapper;

    public IPage<Permission> findPermission() {
        Page<Permission> page = new Page<>(1, 10);
        page.setDesc("permission_str", "resources_type", "description");
        page.setAsc("id", "sys_name", "title");
        return permissionMapper.selectPage(page, new QueryWrapper<>());
    }

    public IPage<Permission> findPermission(QueryPermissionReq query) {
        query.addOrderFieldMapping("id", "id");
        query.addOrderFieldMapping("sysName", "sys_name");
        query.addOrderFieldMapping("title", "title");
        query.addOrderFieldMapping("permissionStr", "permission_str");
        query.addOrderFieldMapping("resourcesType", "resources_type");
        query.addOrderFieldMapping("description", "description");
        query.addOrderFieldMapping("createAt", "create_at");
        query.addOrderFieldMapping("updateAt", "update_at");
        if (query.getOrderFields().size() <= 0) {
            query.addOrderField("createAt", QueryPermissionReq.DESC);
        }
        return query.result(permissionMapper.findByPage(query));
    }
}
