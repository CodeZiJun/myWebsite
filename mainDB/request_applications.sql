create table mainDB.request_applications
(
    id       int auto_increment
        primary key,
    content  text         null,
    position varchar(255) null,
    salary   varchar(255) null,
    phone    varchar(20)  null,
    date     date         null,
    filename varchar(255) null,
    username varchar(255) null,
    reply    text         null
);

