create table MemberGroup (id identity,
					name varchar not null unique,
					profileKey varchar not null unique,
					description varchar,
					hashtag varchar unique,
					leader bigint not null,
					primary key (id),
					foreign key (leader) references Member(id));
					
create table Event (id identity,
					title varchar not null,
					startTime timestamp not null,
					endTime timestamp not null,
					location varchar not null,
					description varchar,
					name varchar not null,
					memberGroup bigint not null,
					primary key (id),
					foreign key (memberGroup) references MemberGroup(id));

create table EventTrack (event bigint not null,
					code varchar,
					name varchar not null,
					description varchar,
					chair bigint not null,
					primary key (event, code),
					foreign key (event) references Event(id),
					foreign key (chair) references Member(id));

create table EventSession (event bigint not null,
					number smallint,
					title varchar not null,
					startTime timestamp not null,
					endTime timestamp not null,
					description varchar,
					hashtag varchar,
					track varchar,
					primary key (event, number),
					foreign key (event) references Event(id),
					foreign key (event, track) references EventTrack(event, code));

create table EventSessionLeader (event bigint not null,
					session bigint not null,
					leader bigint not null,
					primary key (event, session, leader),
					foreign key (event, session) references EventSession(event, number),
					foreign key (leader) references Member(id));

create table EventSessionFavorite (event bigint not null,
					session bigint not null,
					attendee bigint not null,
					primary key (event, session, attendee),
					foreign key (event, session) references EventSession(event, number),
					foreign key (attendee) references Member(id));