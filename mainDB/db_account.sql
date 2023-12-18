create table mainDB.db_account
(
    id            int auto_increment
        primary key,
    username      varchar(256) charset utf8mb4 not null comment '用户名',
    password      varchar(256) charset utf8mb4 not null comment '用户密码',
    email         varchar(256) charset utf8mb4 not null comment '注册邮箱',
    role          varchar(256) charset utf8mb4 null comment '角色字符串',
    register_time datetime                     null comment '注册时间',
    avatar        varchar(255) charset utf8mb4 null comment 'y',
    workplace_id  int                          null comment '工作目录编号',
    archives_id   int                          null comment '档案编号',
    constraint unique_email
        unique (email),
    constraint unique_name
        unique (username)
)
    collate = utf8mb4_bin;

create index db_account_db_archives_id_fk
    on mainDB.db_account (archives_id);

create index db_account_db_workplace_id_fk
    on mainDB.db_account (workplace_id);

