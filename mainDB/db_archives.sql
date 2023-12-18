create table mainDB.db_archives
(
    id             int auto_increment comment '档案编号'
        primary key,
    real_name      varchar(255)      not null comment '真实姓名',
    gender         enum ('男', '女') not null comment '性别',
    telephone      varchar(20)       not null comment '联系电话',
    address        varchar(255)      not null comment '现居住地',
    personal_id    varchar(18)       not null comment '身份证号',
    entry_date     date              null comment '入职时间',
    department_id  int               null comment '部门id',
    position_id    int               null comment '职位编号',
    renew_time     datetime          null comment 'gen',
    personal_intro text              null comment '个人简介',
    education      varchar(255)      null comment '教育经历'
)
    comment '个人档案';

create index db_archives_db_department_id_fk
    on mainDB.db_archives (department_id);

create index db_archives_db_position_id_fk
    on mainDB.db_archives (position_id);

