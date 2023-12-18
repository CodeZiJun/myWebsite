create table mainDB.recruitmsg
(
    id           int auto_increment
        primary key,
    title        varchar(255) null,
    content      text         null,
    department   varchar(255) null,
    publisher_id int          null,
    phone        varchar(20)  null,
    date         date         null
);

