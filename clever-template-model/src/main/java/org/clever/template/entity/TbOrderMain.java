package org.clever.template.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 订单主表(TbOrderMain)实体类
 *
 * @author lizw
 * @since 2020-07-04 16:37:00
 */
@Data
public class TbOrderMain implements Serializable {
    private static final long serialVersionUID = 359796506900143749L;
    /** 订单id */    
    private Long orderId;
    
    /** user_agent_id */    
    private Long userAgentId;
    
    /** 站点ID */    
    private Long siteId;
    
    /** 店铺ID */    
    private Long storeId;
    
    /** 店铺编号 */    
    private String storeNo;
    
    /** 客户id */    
    private Long custId;
    
    /** 订单编号 */    
    private String orderCode;
    
    /** 备注 */    
    private String notes;
    
    /** 订单总金额 */    
    private Double totalPrice;
    
    /** 联系人 */    
    private String linkMan;
    
    /** 联系人电话 */    
    private String linkTel;
    
    /** 实际收货地址 */    
    private String linkAddress;
    
    /** 是否在线支付:1-在线支付,0-线下结算 */    
    private Integer isOnlinePay;
    
    /** 支付状态:0-待支付,1-已支付,2-已取消,3-退款中,4-已退款 5-已完成 */    
    private Integer payStatus;
    
    /** 客户一次提交关联的订单编号，取第一个订单 */    
    private String mainLinkOrder;
    
    /** 订单状态0-已创建,1-待审核,2-待处理,3-已出库,4-已驳回,5-已拒收,6-已完成,7-已取消,8-待支付 9 部分出库，10 正在开票 11 正在出库 */    
    private Integer orderStatus;
    
    /** 订单来源,401-pc,403-wap,406-iso,407-android 408-miniapp */    
    private String orderSource;
    
    /** 是否开发票(1-是,0否) */    
    private String isInvoice;
    
    /** 发票类型(0-无发票,1-普通发票,2-专用发票) */    
    private String invoiceType;
    
    /** 发票-公司名称 */    
    private String invoiceCompany;
    
    /** 发票-纳税人识别号 */    
    private String invoiceNo;
    
    /** 发票-开户银行 */    
    private String invoiceBank;
    
    /** 发票-开户银行号 */    
    private String invoiceBankno;
    
    /** 发票-注册地址 */    
    private String invoiceAddress;
    
    /** 发票-注册电话 */    
    private String invoiceTel;
    
    /** 支付方式(0-在线支付，1-余额支付+在线支付，2-余额支付), 3-线下结算 */    
    private String payType;
    
    /** 直接在线支付金额 */    
    private Double directPayPrice;
    
    /** 余额支付金额 */    
    private Double balancePayPrice;
    
    /** 余额抵扣率 */    
    private Double yedkl;
    
    /** 订单支付单号 */    
    private String orderPayNo;
    
        
    private Date createAt;
    
        
    private Date updateAt;
    
    /** 实付金额 */    
    private Double realPayPrice;
    
    /** 出库时间 */    
    private Date outstockAt;
    
    /** 商品金额 */    
    private Double goodsPrice;
    
    /** 订单渠道 */    
    private String orderChannel;
    
    /** 店铺优惠 */    
    private Double storeDiscount;
    
    /** 平台优惠 */    
    private Double platformDiscount;
    
    /** 支付渠道(1-直连支付宝支付，2-直连微信支付 3-中金接入银联 4-中金支付宝 5-中金微信 6-招行微信 7-招行支付宝) */    
    private String payChannel;
    
    /** 满减优惠总额 */    
    private Double rebateSumPrice;
    
    /** 优惠券总金额(店铺券) */    
    private Double couponSumPrice;
    
    /** 订单配送费 */    
    private Double postage;
    
    /** 订单配送费(优惠价) */    
    private Double preferentialPostage;
    
    /** 是否需要下发ERP，0：不需要 1：需要 */    
    private Integer needErpSyn;
    
    /** 下发状态(0-未下发，1-等待重试，2-下发中，3-下发成功) */    
    private Integer erpSynStatus;
    
    /** 下发次数(重试策略算时间用) */    
    private Integer erpSynCount;
    
    /** 商铺客户ID(下发必要条件) */    
    private String orderStoreCustNo;
    
    /** 最后一次下发时间 */    
    private Date lastErpSynTime;
    
    /** 已退款总金额 */    
    private Double refundSumPrice;
    
    /** 退款状态（0-无退款及申请，1-全部退款，2-部分退款，3-退款失败） */    
    private Integer refundStatus;
    
    /** 子订单流水号(用于招行支付系统) */    
    private String subOrderPayNo;
    
    /** 退款进程状态 0:无退款或全部退款成功;1:有未完成的退款); */    
    private String refundProcessStatus;
    
    /** 退款总金额（申请退款总金额，申请失败需要从此金额扣减） */    
    private Double refundSum;
    
    /** 订单主单编号 */    
    private Long orderMainCode;
    
    /** 订单原始总价，不包含任何优惠的价格  */    
    private Double originalTotalPrice;
    
    /** 套餐组合优惠 */    
    private Double groupSumPrice;
    
    /** 秒杀优惠 */    
    private Double seckillSumPrice;
    
    /** 平台优惠券金额 */    
    private Double platformCouponAmount;
    
    /** 发票url */    
    private String invoiceUrl;
    
    /** 邀请有礼活动是否发券 0:否 1:是 */    
    private Object inviteSend;
    
}