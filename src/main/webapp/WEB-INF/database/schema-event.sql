create table MemberGroup (id identity,
					name varchar not null,
					description varchar,
					hashtag varchar,
					leader bigint not null,
					primary key (id),
					foreign key (leader) references Member(id));
					
create table Event (id identity, 
					title varchar not null,
					description varchar,
					startTime timestamp,
					endTime timestamp,
					location varchar,
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

create table EventSession (code varchar,
					title varchar not null,
					description varchar,
					startTime timestamp,
					endTime timestamp,
					speaker bigint not null,
					event bigint not null,
					track bigint,
					primary key (code, event),
					foreign key (speaker) references Member(id),
					foreign key (event) references Event(id),
					foreign key (track) references EventTrack(id));