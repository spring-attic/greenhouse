create table Leader (id identity,
					name varchar not null,
					company varchar,
					title varchar,
					location varchar,
					bio varchar,
					personalUrl varchar,
					companyUrl varchar,
					twitterUsername varchar,
					member bigint,
					primary key (id),
					foreign key (member) references Member(id));

create table ExternalLeader (leader bigint,
                    sourceId bigint,
                    source varchar,
                    lastUpdated timestamp not null,
                    primary key (leader, sourceId, source),
                    foreign key (leader) references Leader(id)
);

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
					timeZone varchar not null,
					startTime timestamp not null,
					endTime timestamp not null,
					slug varchar not null,
					description varchar,
					memberGroup bigint not null,
					primary key (id),
					foreign key (memberGroup) references MemberGroup(id));

create table ExternalEvent (event bigint,
                    sourceId bigint,
                    source varchar,
                    lastUpdated timestamp not null,
                    primary key (event, sourceId, source),
                    foreign key (event) references Event(id)
);

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

create table EventTimeSlot (
					id identity,
					event bigint not null,
					label varchar,
					startTime timestamp not null,
					endTime timestamp not null,
					primary key (id),
					foreign key (event) references Event(id));

create table ExternalEventTimeSlot (timeSlot bigint,
                    sourceId bigint,
                    source varchar,
                    lastUpdated timestamp not null,
                    primary key (timeSlot, sourceId, source),
                    foreign key (timeSlot) references EventTimeSlot(id)
);


create table EventSession (event bigint,
					id int not null,
					title varchar not null,
					description varchar,
					hashtag varchar,
					track varchar,
					venue bigint,
					room varchar,
					rating real,
					master bigint,
					timeslot bigint,
					primary key (event, id),
					foreign key (event) references Event(id),
					foreign key (event, track) references EventTrack(event, code),
					foreign key (venue, room) references VenueRoom(venue, id),					
					foreign key (master) references Session(id),
					foreign key (timeslot) references EventTimeSlot(id));

create table ExternalEventSession (event bigint not null,
					sessionId int not null,
                    sourceId bigint not null,
                    source varchar not null,
                    lastUpdated timestamp not null,
                    primary key (event, sessionId, sourceId, source)
);

create table EventSessionLeader (event bigint,
					session int,
					leader bigint,
					rank tinyint,					
					primary key (event, session, leader),
					foreign key (event, session) references EventSession(event, id),
					foreign key (leader) references Leader(id),
					constraint UniqueEventSessionLeaderRank unique(event, session, rank));

create table LeaderInviteSession (invite varchar,
					event bigint,
					session smallint,
					leader bigint not null,
					primary key (invite, event, session),
					foreign key (invite) references Invite(token),
					foreign key (event, session) references EventSession(event, id),
					foreign key (leader) references Leader(id));
					
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