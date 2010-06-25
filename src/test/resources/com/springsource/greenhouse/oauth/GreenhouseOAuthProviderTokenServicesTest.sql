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
					
insert into User (firstName, lastName, email, password, username) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson');
insert into Consumer (consumerKey, name, description, website, callbackUrl, secret, ownerId) values ('a08318eb478a1ee31f69a55276f3af64', 'Greenhouse for the iPhone', 'Awesome', 'http://www.springsource.com', 'x-com-springsource-greenhouse://oauth-response', '80e7f8f7ba724aae9103f297e5fb9bdf', 1);