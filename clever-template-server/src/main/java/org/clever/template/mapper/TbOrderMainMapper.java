package org.clever.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.clever.template.entity.TbOrderMain;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/04 22:34 <br/>
 */
@Repository
@Mapper
public interface TbOrderMainMapper extends BaseMapper<TbOrderMain> {
}
