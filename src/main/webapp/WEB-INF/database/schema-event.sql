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

create table EventTrack (event bigint,
					code varchar,
					name varchar not null,
					description varchar,
					chair bigint not null,
					primary key (event, code),
					foreign key (event) references Event(id),
					foreign key (chair) references Member(id));

create table EventSession (event bigint,
					number smallint,
					title varchar not null,
					startTime timestamp not null,
					endTime timestamp not null,
					description varchar,
					hashtag varchar,
					track varchar,
					rating real,
					primary key (event, number),
					foreign key (event) references Event(id),
					foreign key (event, track) references EventTrack(event, code));

create table EventSessionLeader (event bigint,
					session bigint,
					leader bigint,
					primary key (event, session, leader),
					foreign key (event, session) references EventSession(event, number),
					foreign key (leader) references Member(id));

create table EventSessionFavorite (event bigint,
					session bigint,
					attendee bigint,
					primary key (event, session, attendee),
					foreign key (event, session) references EventSession(event, number),
					foreign key (attendee) references Member(id));

create table EventSessionRating (event bigint,
					session bigint,
					attendee bigint,
					rating tinyint not null check (rating in (1, 2, 3, 4, 5)),
					comment varchar,
					primary key (event, session, attendee),
					foreign key (event, session) references EventSession(event, number),
					foreign key (attendee) references Member(id));
					
create table EventTweetAction (memberAction bigint, 
					event bigint not null,
					session smallint,
					varchar tweet not null,
					primary key (id),
					foreign key (memberAction) references MemberAction(id),
					foreign key (event) references Event(id),
					foreign key (event, session) references EventSession(event, number));