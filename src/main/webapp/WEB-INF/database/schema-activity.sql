create table MemberAction (id identity,
					actionType varchar not null,
					performTime timestamp not null,
					latitude real,
					longitude real,
					member bigint,
					primary key (id),					
					foreign key (member) references Member(id));

create table Badge (name varchar,
					description varchar not null,
					level varchar not null,
					icon varchar not null,
					primary key (name));
					
create table AwardedBadge(id identity,
					name varchar not null,
					awardTime timestamp not null,
					member bigint not null,
					memberAction bigint,
					primary key (id),
					foreign key (name) references Badge(name),					
					foreign key (member) references Member(id),
					foreign key (memberAction) references MemberAction(id));