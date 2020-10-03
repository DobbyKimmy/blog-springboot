create table blog(
id bigint primary key auto_increment,
user_id bigint,
title varchar(100),
content text,
description varchar(100),
created_at datetime default now(),
updated_at datetime default now()
)