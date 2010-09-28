create table Venue (id identity,
					name varchar not null,
					postalAddress varchar not null,
					locationHint varchar not null,
					latitude real not null,
					longitude real not null,					
					createdBy bigint not null,
					primary key (id),
					foreign key (createdBy) references Member(id));
					
create table VenueRoom (venue bigint,
					name varchar not null,
					capacity int not null,
					locationHint varchar not null,
					primary key (venue, name),
					foreign key (venue) references Venue(id)); 