create database if not exists `clever-template` default character set = utf8;
use `clever-template`;


/* ====================================================================================================================
    permission -- 权限表
==================================================================================================================== */
create table permission
(
    id              bigint          not null        auto_increment                          comment '主键id',
    sys_name        varchar(127)    not null                                                comment '系统(或服务)名称',
    title           varchar(255)    not null                                                comment '权限标题',
    permission_str  varchar(255)    not null        unique                                  comment '唯一权限标识字符串',
    resources_type  int(1)          not null        default 1                               comment '权限类型，1:web资源权限, 2:菜单权限，3:ui权限，......',
    description     varchar(1203)                                                           comment '权限说明',
    create_at       datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at       datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '权限表';
create index permission_sys_name on permission (sys_name);
create index permission_permission_str on permission (permission_str);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    auto_increment_id -- 自增长ID数据表
==================================================================================================================== */
create table auto_increment_id
(
    id                  bigint          not null        auto_increment                          comment '主键id',
    company_flag        varchar(31)     not null                                                comment '抽检公司标识',
    business_type       varchar(127)    not null                                                comment '业务类型',
    prefix              varchar(127)    not null                                                comment 'ID前缀',
    current_value       bigint          not null        default -1                              comment '当前值',
    description         varchar(511)                                                            comment '说明',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '自增长ID数据表';
create index auto_increment_id_company_flag on auto_increment_id (company_flag);
create index auto_increment_id_business_type on auto_increment_id (business_type);
create index auto_increment_id_prefix on auto_increment_id (prefix);
create unique index auto_increment_id_unique_prefix on auto_increment_id (company_flag, business_type, prefix);
create unique index auto_increment_id_unique_value on auto_increment_id (company_flag, business_type, prefix, current_value);
-- [存储过程]获取自增长ID
delimiter $
-- create definer = `dbxia-0001`@`%` procedure auto_increment_id_next
create procedure auto_increment_id_next(
    in p_company_flag       varchar(127),   -- 业务类型
    in p_business_type      varchar(127),   -- 业务类型
    in p_prefix             varchar(127),   -- ID前缀
    in p_step               bigint,         -- ID步进长度(必须大于0,默认为1)
    out p_old_value         bigint,         -- ID自动增长之前的值
    out p_current_value     bigint          -- ID自动增长后的值
)
begin
    -- 定义current_value的默认值,与建表语句中的default保持一致
    declare var_current_default_value bigint default -1;
    -- 数据主键
    declare var_data_primary_key bigint default null;
    -- p_step 默认值为 1
    if (p_step is null or trim(p_step)='' or p_step<=0 ) then
        set p_step = 1;
    end if;
    -- 查询数据主键
    select
        id, current_value into var_data_primary_key, p_old_value
    from auto_increment_id
    where company_flag=p_company_flag and business_type=p_business_type and prefix=p_prefix;
    -- 开启事务
    start transaction;
    if (var_data_primary_key is null or trim(var_data_primary_key)='' or p_old_value is null) then
        -- 插入新数据
        insert into auto_increment_id
        (company_flag, business_type, prefix, description)
        values
        (p_company_flag, p_business_type, p_prefix, '系统自动生成')
        on duplicate key update update_at=now();
        -- 查询数据主键
        select
            id, current_value into var_data_primary_key, p_old_value
        from auto_increment_id
        where company_flag=p_company_flag and business_type=p_business_type and prefix=p_prefix;
    end if;
    -- 更新ID数据(使用Mysql行级锁保证并发性)
    update auto_increment_id set current_value=current_value+p_step where id=var_data_primary_key;
    -- 查询更新之后的值
    select current_value into p_current_value from auto_increment_id where id=var_data_primary_key;
    set p_old_value = p_current_value - p_step;
    -- 提交事务
    commit;
end
$
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


create table tb_order_detail_distinct
(
    id                  bigint                                      not null comment '主键id' primary key,
    store_no            varchar(31)                                 not null comment '店铺编号',
    order_code          varchar(63)                                 not null comment '订单编码(唯一)',
    store_prod_no       varchar(31)                                 not null comment '店铺商品编码',
    erp_no              varchar(31)                                 null comment 'ERP编码',
    prod_name           varchar(255)                                null comment '商品名称',
    prod_specification  varchar(255)                                null comment '规格',
    package_unit        varchar(15)                                 null comment '单位',
    manufacture         varchar(255)                                null comment '厂家',
    front_pic           varchar(200)                                null comment '正面图片地址',
    merchandise_number  decimal(14, 5)                              not null comment '购买数量',
    out_number          decimal(14, 5) default 0.00000              not null comment '出库数量',
    no_out_number       decimal(14, 5) default 0.00000              not null comment '不出库数量',
    goods_status        int(3)         default 0                    not null comment '（0-未出库，1-已出库，2-不出库，3-部分出库)',
    member_price        decimal(20, 5)                              not null comment '会员价(原价)',
    average_price       decimal(20, 5)                              not null comment '均摊价',
    seckill_price       decimal(20, 5)                              null comment '秒杀价格',
    seckill_number      decimal(20, 5)                              null comment '秒杀数量',
    create_at           datetime(3)    default CURRENT_TIMESTAMP(3) not null comment '创建时间',
    update_at           datetime(3)                                 null on update CURRENT_TIMESTAMP(3) comment '更新时间',
    refund_number       decimal(14, 5) default 0.00000              not null comment '已退款数量',
    refund_apply_number decimal(14, 5) default 0.00000              not null comment '退款申请中数量',
    store_discount      decimal(20, 5) default 0.00000              not null comment '单个商品店铺优惠',
    platform_discount   decimal(20, 5) default 0.00000              not null comment '单个商品平台优惠'
) comment '订单去重明细表';
create index idx_tb_order_detail_distinct_order_code on tb_order_detail_distinct (order_code);
create index idx_tb_order_detail_distinct_store_no on tb_order_detail_distinct (store_no);
create index idx_tb_order_detail_distinct_store_prod_no on tb_order_detail_distinct (store_prod_no);

create table tb_order_main
(
    order_id               bigint                                   not null comment '订单id' primary key,
    user_agent_id          bigint                                   not null comment 'user_agent_id',
    site_id                bigint                                   not null comment '站点ID',
    store_id               bigint                                   null comment '店铺ID',
    store_no               varchar(20)    default ''                null comment '店铺编号',
    cust_id                bigint                                   not null comment '客户id',
    order_code             varchar(50)    default ''                not null comment '订单编号',
    notes                  varchar(3800)  default ''                null comment '备注',
    total_price            decimal(20, 5)                           null comment '订单总金额',
    link_man               varchar(50)                              null comment '联系人',
    link_tel               varchar(20)    default ''                null comment '联系人电话',
    link_address           varchar(300)   default ''                null comment '实际收货地址',
    is_online_pay          int            default 0                 null comment '是否在线支付:1-在线支付,0-线下结算',
    pay_status             int            default 0                 null comment '支付状态:0-待支付,1-已支付,2-已取消,3-退款中,4-已退款 5-已完成',
    main_link_order        varchar(50)                              null comment '客户一次提交关联的订单编号，取第一个订单',
    order_status           int            default 0                 null comment '订单状态0-已创建,1-待审核,2-待处理,3-已出库,4-已驳回,5-已拒收,6-已完成,7-已取消,8-待支付 9 部分出库，10 正在开票 11 正在出库',
    order_source           varchar(5)                               null comment '订单来源,401-pc,403-wap,406-iso,407-android 408-miniapp',
    is_invoice             char           default ''                null comment '是否开发票(1-是,0否)',
    invoice_type           char           default '0'               null comment '发票类型(0-无发票,1-普通发票,2-专用发票)',
    invoice_company        varchar(100)   default ''                null comment '发票-公司名称',
    invoice_no             varchar(100)   default ''                null comment '发票-纳税人识别号',
    invoice_bank           varchar(100)   default ''                null comment '发票-开户银行',
    invoice_bankno         varchar(50)    default ''                null comment '发票-开户银行号',
    invoice_address        varchar(50)    default ''                null comment '发票-注册地址',
    invoice_tel            varchar(50)    default ''                null comment '发票-注册电话',
    pay_type               varchar(2)     default '0'               null comment '支付方式(0-在线支付，1-余额支付+在线支付，2-余额支付), 3-线下结算',
    direct_pay_price       decimal(20, 5)                           null comment '直接在线支付金额',
    balance_pay_price      decimal(20, 5)                           null comment '余额支付金额',
    yedkl                  decimal(22, 20)                          null comment '余额抵扣率',
    order_pay_no           varchar(64)    default ''                null comment '订单支付单号',
    create_at              timestamp      default CURRENT_TIMESTAMP null,
    update_at              timestamp      default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    real_pay_price         decimal(20, 5)                           null comment '实付金额',
    outstock_at            timestamp                                null comment '出库时间',
    goods_price            decimal(20, 5) default 0.00000           null comment '商品金额',
    order_channel          char           default '0'               not null comment '订单渠道',
    store_discount         decimal(20, 5) default 0.00000           null comment '店铺优惠',
    platform_discount      decimal(20, 5) default 0.00000           null comment '平台优惠',
    pay_channel            char                                     null comment '支付渠道(1-直连支付宝支付，2-直连微信支付 3-中金接入银联 4-中金支付宝 5-中金微信 6-招行微信 7-招行支付宝)',
    rebate_sum_price       decimal(20, 5) default 0.00000           not null comment '满减优惠总额',
    coupon_sum_price       decimal(20, 5) default 0.00000           not null comment '优惠券总金额(店铺券)',
    postage                decimal(20, 5) default 0.00000           not null comment '订单配送费',
    preferential_postage   decimal(20, 5)                           null comment '订单配送费(优惠价)',
    need_erp_syn           int            default 0                 not null comment '是否需要下发ERP，0：不需要 1：需要',
    erp_syn_status         int            default 0                 null comment '下发状态(0-未下发，1-等待重试，2-下发中，3-下发成功)',
    erp_syn_count          int            default 0                 not null comment '下发次数(重试策略算时间用)',
    order_store_cust_no    varchar(127)                             null comment '商铺客户ID(下发必要条件)',
    last_erp_syn_time      datetime(3)                              null comment '最后一次下发时间',
    refund_sum_price       decimal(20, 5) default 0.00000           not null comment '已退款总金额',
    refund_status          int            default 0                 not null comment '退款状态（0-无退款及申请，1-全部退款，2-部分退款，3-退款失败）',
    sub_order_pay_no       varchar(50)    default ''                not null comment '子订单流水号(用于招行支付系统)',
    refund_process_status  char           default '0'               null comment '退款进程状态 0:无退款或全部退款成功;1:有未完成的退款);',
    refund_sum             decimal(20, 5) default 0.00000           not null comment '退款总金额（申请退款总金额，申请失败需要从此金额扣减）',
    order_main_code        bigint         default 0                 not null comment '订单主单编号',
    original_total_price   decimal(20, 5) default 0.00000           not null comment '订单原始总价，不包含任何优惠的价格 ',
    group_sum_price        decimal(20, 5) default 0.00000           not null comment '套餐组合优惠',
    seckill_sum_price      decimal(20, 5) default 0.00000           not null comment '秒杀优惠',
    platform_coupon_amount decimal(20, 5) default 0.00000           not null comment '平台优惠券金额',
    invoice_url            varchar(500)   default ''                not null comment '发票url',
    invite_send            tinyint(1)     default 0                 not null comment '邀请有礼活动是否发券 0:否 1:是',
    constraint order_main_ordercode  unique (order_code)
) comment '订单主表';

create index idx_order_main_code on tb_order_main (order_main_code);
create index order_main_custid on tb_order_main (cust_id);
create index order_main_payno on tb_order_main (order_pay_no);

