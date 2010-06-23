drop table UserApp if exists;
drop table App if exists;
drop table User if exists;

create table User (id identity,
					firstName varchar not null, 
					lastName varchar not null,
					email varchar(320) not null unique,
					password varchar not null,
					username varchar(15) unique,
					primary key (id));

create table App (id identity,
					name varchar not null unique, 
					ownerId bigint not null,
					description varchar not null,
					website varchar not null unique,
					callbackUrl varchar,
					consumerKey varchar not null unique,
					secret varchar not null,
					primary key (id),
					foreign key (ownerId) references User(id));
					
create table UserApp (userId bigint not null,
					appId bigint not null,
					token varchar not null unique,
					primary key (userId, appId),
					foreign key (userId) references User(id),
					foreign key (appId) references App(id));