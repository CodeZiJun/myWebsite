create table mainDB.recruitment_applications
(
    id                 int auto_increment
        primary key,
    position           varchar(255) not null,
    status             varchar(100) not null,
    phone              varchar(20)  not null,
    application_date   date         not null,
    resume_path        varchar(255) not null,
    applicant_username varchar(255) not null,
    salary             varchar(255) null
);

