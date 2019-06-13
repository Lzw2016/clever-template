package org.clever.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.clever.template.entity.Permission;
import org.springframework.stereotype.Repository;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-06-13 18:43 <br/>
 */
@Repository
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {


}
