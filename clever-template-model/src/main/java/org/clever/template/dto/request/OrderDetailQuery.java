package org.clever.template.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/04 22:37 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderDetailQuery extends QueryByPage {
    /**
     * 店铺编号
     */
    private String storeNo;

    /**
     * 订单编码(唯一)
     */
    private String orderCode;

    /**
     * 店铺商品编码
     */
    private String storeProdNo;

    /**
     * ERP编码
     */
    private String erpNo;

    /**
     * 商品名称
     */
    private String prodName;
}
