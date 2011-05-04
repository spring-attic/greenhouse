create table Venue (id identity,
					name varchar not null,
					postalAddress varchar not null,
					latitude double not null,
					longitude double not null,
					locationHint varchar not null,
					createdBy bigint not null,
					primary key (id),
					foreign key (createdBy) references Member(id));
					
create table VenueRoom (venue bigint,
					id smallint not null,
					name varchar not null,
					capacity int not null,
					locationHint varchar not null,
					primary key (venue, id),
					foreign key (venue) references Venue(id) on delete cascade); 