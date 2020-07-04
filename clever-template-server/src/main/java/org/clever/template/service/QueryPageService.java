package org.clever.template.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.clever.template.dto.request.OrderDetailQuery;
import org.clever.template.dto.request.OrderDetailQuery2;
import org.clever.template.dto.request.QueryPermissionReq;
import org.clever.template.entity.Permission;
import org.clever.template.entity.TbOrderDetailDistinct;
import org.clever.template.mapper.PermissionMapper;
import org.clever.template.mapper.TbOrderDetailDistinctMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-06-13 18:42 <br/>
 */
@SuppressWarnings("DuplicatedCode")
@Transactional(readOnly = true)
@Service
@Slf4j
public class QueryPageService {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private TbOrderDetailDistinctMapper tbOrderDetailDistinctMapper;

    @SuppressWarnings("deprecation")
    public IPage<Permission> findPermission() {
        Page<Permission> page = new Page<>(1, 10);
        page.setDesc("permission_str", "resources_type", "description");
        page.setAsc("id", "sys_name", "title");
        permissionMapper.selectPage(page, new QueryWrapper<>());
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
        // if (query.getOrderFields().size() <= 0) {
        //     query.addOrderField("createAt", QueryPermissionReq.DESC);
        // }
        // permissionMapper.findByPage(query);
        return query.result(permissionMapper.findByPage(query));
    }

    public IPage<TbOrderDetailDistinct> findOrderDetail(OrderDetailQuery query) {
        query.addOrderFieldMapping("id", "id");
        query.addOrderFieldMapping("storeNo", "store_no");
        query.addOrderFieldMapping("orderCode", "order_code");
        query.addOrderFieldMapping("storeProdNo", "store_prod_no");
        query.addOrderFieldMapping("erpNo", "erp_no");
        query.addOrderFieldMapping("prodName", "prod_name");
        query.addOrderFieldMapping("prodSpecification", "prod_specification");
        query.addOrderFieldMapping("packageUnit", "package_unit");
        query.addOrderFieldMapping("manufacture", "manufacture");
        query.addOrderFieldMapping("frontPic", "front_pic");
        query.addOrderFieldMapping("merchandiseNumber", "merchandise_number");
        query.addOrderFieldMapping("outNumber", "out_number");
        query.addOrderFieldMapping("noOutNumber", "no_out_number");
        query.addOrderFieldMapping("goodsStatus", "goods_status");
        query.addOrderFieldMapping("memberPrice", "member_price");
        query.addOrderFieldMapping("averagePrice", "average_price");
        query.addOrderFieldMapping("seckillPrice", "seckill_price");
        query.addOrderFieldMapping("seckillNumber", "seckill_number");
        query.addOrderFieldMapping("createAt", "create_at");
        query.addOrderFieldMapping("updateAt", "update_at");
        query.addOrderFieldMapping("refundNumber", "refund_number");
        query.addOrderFieldMapping("refundApplyNumber", "refund_apply_number");
        query.addOrderFieldMapping("storeDiscount", "store_discount");
        query.addOrderFieldMapping("platformDiscount", "platform_discount");
        return query.result(tbOrderDetailDistinctMapper.findOrderDetail(query));
    }

    public List<TbOrderDetailDistinct> findOrderDetail2(OrderDetailQuery2 query) {
        return tbOrderDetailDistinctMapper.findOrderDetail2(query);
    }
}
