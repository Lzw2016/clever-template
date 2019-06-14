package org.clever.template.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-06-14 12:45 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryPermissionReq extends QueryByPage {

    /**
     * 系统(或服务)名称
     */
    private String sysName;

    /**
     * 权限标题
     */
    private String title;

    /**
     * 唯一权限标识字符串
     */
    private String permissionStr;
}
