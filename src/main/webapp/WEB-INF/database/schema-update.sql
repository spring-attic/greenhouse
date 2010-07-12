create table Update (id identity, 
					text varchar not null,
					updateTimestamp bigint not null,
					member bigint,
					primary key (id),
					foreign key (member) references Member(id));