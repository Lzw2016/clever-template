package org.clever.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.template.dto.request.QueryPermissionReq;
import org.clever.template.entity.Permission;
import org.clever.template.model.NextId;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-06-13 18:43 <br/>
 */
@Repository
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> findByPage(@Param("query") QueryPermissionReq query);

    void nextId(@Param("nextId") NextId nextId);

    void updateCurrentValue(
            @Param("companyFlag") String companyFlag,
            @Param("businessType") String businessType,
            @Param("prefix") String prefix,
            @Param("step") Long step);
}
