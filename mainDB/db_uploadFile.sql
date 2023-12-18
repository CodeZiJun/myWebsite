create table mainDB.db_uploadFile
(
    id           bigint auto_increment
        primary key,
    real_name    varchar(255) null comment '文件实际名称',
    file_name    varchar(255) null comment '文件名',
    primary_name varchar(255) null comment '文件主名',
    extension    varchar(255) null comment '文件扩展名',
    type         varchar(255) null comment '文件类型',
    path         varchar(255) null comment '存放路径',
    size         bigint       null comment '大小',
    uploader     varchar(255) null comment '上传人',
    create_time  datetime     null comment '创建日期'
)
    row_format = DYNAMIC;

