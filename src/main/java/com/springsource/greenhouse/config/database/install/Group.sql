create table MemberGroup (id identity,
					name varchar not null unique,
					slug varchar not null unique,
					description varchar,
					hashtag varchar unique,
					leader bigint not null,
					primary key (id),
					foreign key (leader) references Member(id));