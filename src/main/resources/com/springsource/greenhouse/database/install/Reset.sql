create table ResetPassword (token varchar,
					member bigint not null,
					primary key (token),
					foreign key (member) references Member(id));