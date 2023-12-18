create table mainDB.db_salary
(
    id     int auto_increment comment '薪资编号'
        primary key,
    amount int not null comment '金额'
)
    comment '薪资对照表';

