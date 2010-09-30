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
					timezone varchar not null,
					startTime timestamp not null,
					endTime timestamp not null,
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
					id smallint auto_increment,
					startTime timestamp not null,
					endTime timestamp not null,
					title varchar not null unique,
					description varchar,
					hashtag varchar,
					track varchar,
					venue bigint,
					room varchar,
					rating real,
					master bigint,
					primary key (event, id),
					foreign key (event) references Event(id),
					foreign key (event, track) references EventTrack(event, code),
					foreign key (venue, room) references VenueRoom(venue, id),					
					foreign key (master) references Session(id));
				
create table EventSessionLeader (event bigint,
					session smallint,
					leader bigint,
					rank tinyint,
					primary key (event, session, leader),
					foreign key (event, session) references EventSession(event, id),
					foreign key (leader) references Member(id),
					constraint UniqueEventSessionLeaderRank unique(event, session, rank));

create table EventSessionFavorite (event bigint,
					session smallint,
					attendee bigint,
					rank smallint,
					primary key (event, session, attendee),
					foreign key (event, session) references EventSession(event, id),
					foreign key (attendee) references Member(id),
					constraint UniqueEventSessionFavoriteRank unique(event, attendee, rank));

create table EventSessionRating (event bigint,
					session smallint,
					attendee bigint,
					rating tinyint not null check (rating in (1, 2, 3, 4, 5)),
					comment varchar,
					primary key (event, session, attendee),
					foreign key (event, session) references EventSession(event, id),
					foreign key (attendee) references Member(id));
					
create table EventTweetAction (memberAction bigint, 
					event bigint not null,
					session smallint,
					tweet varchar not null,
					primary key (memberAction),
					foreign key (memberAction) references MemberAction(id),
					foreign key (event) references Event(id),
					foreign key (event, session) references EventSession(event, id));