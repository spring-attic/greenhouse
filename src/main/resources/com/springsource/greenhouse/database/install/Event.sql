create table MemberSession (member bigint not null,
					number smallint not null,
					title varchar not null unique,
					slug varchar not null unique,
					description varchar,
					hashtag varchar,
					primary key (member, number),
					foreign key (member) references Member(id));

create table Event (id identity,
					title varchar not null,
					startTime timestamp not null,
					endTime timestamp not null,
					slug varchar not null,
					location varchar not null,
					description varchar,
					memberGroup bigint not null,
					primary key (id),
					foreign key (memberGroup) references MemberGroup(id));

create table EventVenue (event bigint,
					venue bigint,
					primary key (event, venue),
					foreign key (event) references Event(id),
					foreign key (venue) references Venue(id));
					
create table EventTrack (event bigint,
					code varchar,
					name varchar not null,
					description varchar,
					chair bigint not null,
					primary key (event, code),
					foreign key (event) references Event(id),
					foreign key (chair) references Member(id));

create table EventSession (event bigint,
					member bigint,
					number smallint,
					delivery smallint, 
					startTime timestamp not null,
					endTime timestamp not null,
					track varchar,
					venue bigint,
					room varchar,
					rating real,
					primary key (event, member, number, delivery),
					foreign key (event) references Event(event),
					foreign key (member, number) references MemberSession(member, number));
				
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
					tweet varchar not null,
					primary key (memberAction),
					foreign key (memberAction) references MemberAction(id),
					foreign key (event) references Event(id),
					foreign key (event, session) references EventSession(event, number));