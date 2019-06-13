package org.clever.template.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
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
        IPage<Permission> page = permissionMapper.selectPage(new Page<>(1, 10), new QueryWrapper<>());
        return page;
    }
}
