package org.clever.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.template.dto.request.OrderDetailQuery;
import org.clever.template.dto.request.OrderDetailQuery2;
import org.clever.template.entity.TbOrderDetailDistinct;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/04 22:34 <br/>
 */
@Repository
@Mapper
public interface TbOrderDetailDistinctMapper extends BaseMapper<TbOrderDetailDistinct> {

    List<TbOrderDetailDistinct> findOrderDetail(@Param("query") OrderDetailQuery query);

    List<TbOrderDetailDistinct> findOrderDetail2(@Param("query") OrderDetailQuery2 query);
}
