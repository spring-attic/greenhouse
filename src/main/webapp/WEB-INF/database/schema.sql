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

create table Consumer (consumerKey varchar not null,
					name varchar not null unique, 
					description varchar not null,
					website varchar not null unique,
					callbackUrl varchar,
					secret varchar not null,
					ownerId bigint not null,
					primary key (consumerKey),
					foreign key (ownerId) references User(id));
					
create table AuthorizedConsumer (userId bigint not null,
					consumerKey varchar not null,
					accessToken varchar not null unique,
					primary key (userId, consumerKey),
					foreign key (userId) references User(id),
					foreign key (consumerKey) references Consumer(consumerKey));
					
create table OAuthToken (tokenValue varchar not null unique,
					consumerKey varchar not null,
					secret varchar not null,
					callbackUrl varchar,
					updateTimestamp bigint not null,
					verifier varchar,
					userId bigint, 
					primary key (tokenValue),
					foreign key (consumerKey) references Consumer(consumerKey),
					foreign key (userId) references User(id));