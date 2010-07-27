create table MemberGroup (id identity,
					name varchar not null,
					description varchar,
					profileKey varchar unique,					
					searchString varchar,
					leader bigint not null,
					primary key (id),
					foreign key (leader) references Member(id));
					
create table Event (id identity,
					title varchar not null,
					startDate date,
					endDate date,
					location varchar,
					description varchar,
					memberGroup bigint not null,
					primary key (id),
					foreign key (memberGroup) references MemberGroup(id));

create table EventTrack (id identity,
					name varchar not null,
					description varchar,
					chair bigint not null,
					event bigint not null,
					primary key (id),
					foreign key (chair) references Member(id),
					foreign key (event) references Event(id));

create table EventSession (code smallint,
					title varchar not null,
					startTime timestamp,
					endTime timestamp,
					description varchar,
					event bigint not null,
					track bigint,
					primary key (code, event),
					foreign key (event) references Event(id),
					foreign key (track) references EventTrack(id));

create table EventSessionLeader (session smallint not null,
					leader bigint not null,
					primary key (session, leader),
					foreign key (session) references EventSession(code),
					foreign key (leader) references Member(id));	