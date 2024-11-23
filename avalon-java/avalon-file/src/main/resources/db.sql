## 文件表
create table file_upload
(
    id          int auto_increment
        primary key,
    creator     int          null,
    create_time datetime     null,
    name        varchar(255) null,
    update_time datetime     null,
    updater     int          null,
    origin_name varchar(256) null,
    url         varchar(256) null,
    mime        varchar(256) null,
    ext         varchar(256) null,
    file_path   varchar(256) null,
    thumb_path  varchar(256) null
);