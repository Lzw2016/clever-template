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

