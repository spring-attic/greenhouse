create table ResetPassword (token varchar,
					createTime timestamp not null default now(),
					member bigint not null,
					primary key (token),
					foreign key (member) references Member(id));