create table mainDB.db_department
(
    id              int auto_increment comment '部门编号'
        primary key,
    department_name varchar(255) not null comment '部门名',
    minister_id     int          null comment '部长用户id',
    constraint db_department_pk
        unique (department_name)
)
    comment '部门';

create index db_department_db_account_id_fk
    on mainDB.db_department (minister_id);

