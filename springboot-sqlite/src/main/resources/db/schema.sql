drop table if exists t_student;

create table t_student
(
    dbid   integer primary key autoincrement not null,
    name   varchar(32) not null,
    age    integer     null,
    gender integer     null,
    phone  varchar(32) not null
);
