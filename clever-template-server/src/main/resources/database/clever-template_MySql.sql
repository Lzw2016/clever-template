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


