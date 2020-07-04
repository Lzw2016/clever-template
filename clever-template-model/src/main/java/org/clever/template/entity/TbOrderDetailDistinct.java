package org.clever.template.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 订单去重明细表(TbOrderDetailDistinct)实体类
 *
 * @author lizw
 * @since 2020-07-04 16:37:00
 */
@Data
public class TbOrderDetailDistinct implements Serializable {
    private static final long serialVersionUID = 710896667538058774L;
    /** 主键id */    
    private Long id;
    
    /** 店铺编号 */    
    private String storeNo;
    
    /** 订单编码(唯一) */    
    private String orderCode;
    
    /** 店铺商品编码 */    
    private String storeProdNo;
    
    /** ERP编码 */    
    private String erpNo;
    
    /** 商品名称 */    
    private String prodName;
    
    /** 规格 */    
    private String prodSpecification;
    
    /** 单位 */    
    private String packageUnit;
    
    /** 厂家 */    
    private String manufacture;
    
    /** 正面图片地址 */    
    private String frontPic;
    
    /** 购买数量 */    
    private Double merchandiseNumber;
    
    /** 出库数量 */    
    private Double outNumber;
    
    /** 不出库数量 */    
    private Double noOutNumber;
    
    /** （0-未出库，1-已出库，2-不出库，3-部分出库) */    
    private Integer goodsStatus;
    
    /** 会员价(原价) */    
    private Double memberPrice;
    
    /** 均摊价 */    
    private Double averagePrice;
    
    /** 秒杀价格 */    
    private Double seckillPrice;
    
    /** 秒杀数量 */    
    private Double seckillNumber;
    
    /** 创建时间 */    
    private Date createAt;
    
    /** 更新时间 */    
    private Date updateAt;
    
    /** 已退款数量 */    
    private Double refundNumber;
    
    /** 退款申请中数量 */    
    private Double refundApplyNumber;
    
    /** 单个商品店铺优惠 */    
    private Double storeDiscount;
    
    /** 单个商品平台优惠 */    
    private Double platformDiscount;
    
}