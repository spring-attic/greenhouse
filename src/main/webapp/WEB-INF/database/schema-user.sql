create table User (id identity,
					firstName varchar not null, 
					lastName varchar not null,
					email varchar(320) not null unique,
					password varchar not null,
					username varchar(15) unique,
					primary key (id));

create table Consumer (consumerKey varchar,
					name varchar not null unique, 
					description varchar not null,
					website varchar not null unique,
					callbackUrl varchar,
					secret varchar not null,
					ownerId bigint not null,
					primary key (consumerKey),
					foreign key (ownerId) references User(id));
					
create table AuthorizedConsumer (userId bigint,
					consumerKey varchar,
					accessToken varchar not null,
					secret varchar not null,
					primary key (userId, consumerKey),
					foreign key (userId) references User(id),
					foreign key (consumerKey) references Consumer(consumerKey));
					
create table OAuthToken (tokenValue varchar,
					consumerKey varchar not null,
					secret varchar not null,
					callbackUrl varchar,
					updateTimestamp bigint not null,
					verifier varchar,
					userId bigint, 
					primary key (tokenValue),
					foreign key (consumerKey) references Consumer(consumerKey),
					foreign key (userId) references User(id));

create table NetworkConnection (userId bigint,
                    network varchar,
                    accessToken varchar not null,
                    secret varchar not null,
                    primary key (userId, network),
                    foreign key (userId) references User(id));
                    
create table Update (id identity, 
					text varchar not null,
					updateTimestamp bigint not null,
					userId bigint,
					primary key (id),
					foreign key (userId) references User(id));
					