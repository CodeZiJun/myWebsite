create table mainDB.db_position
(
    id            int auto_increment comment '职位编号'
        primary key,
    position_name varchar(255) not null comment '职位名称',
    salary_id     int          not null comment '薪资编号',
    constraint db_position_pk
        unique (position_name)
)
    comment '职位';

create index db_position_db_salary_id_fk
    on mainDB.db_position (salary_id);

