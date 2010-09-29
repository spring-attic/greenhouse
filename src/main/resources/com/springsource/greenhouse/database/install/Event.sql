create table Session (id identity,
					title varchar not null,
					slug varchar not null,
					description varchar,
					hashtag varchar,
					public boolean default false,
					primary key (id));

create table SessionDeveloper (session bigint,
					developer bigint,
					primary key (session, developer),
					foreign key (session) references Session(id) on delete cascade,
					foreign key (developer) references Member(id));
					
create table Event (id identity,
					title varchar not null,
					startTime timestamp not null,
					endTime timestamp not null,
					timezone varchar not null,
					slug varchar not null,
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
					number smallint,
					title varchar not null unique,
					startTime timestamp not null,
					endTime timestamp not null,
					description varchar,
					hashtag varchar,
					track varchar,
					venue bigint,
					room varchar,
					rating real,
					master bigint,
					primary key (event, number),
					foreign key (event) references Event(event),
					foreign key (event, track) references Track(event, code),
					foreign key (sessionccefer1) references Session(id));
				
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