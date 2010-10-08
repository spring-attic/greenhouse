create table ResetPassword (token varchar,
					createTime bigint not null default now(),
					member bigint not null,
					primary key (token),
					foreign key (member) references Member(id));