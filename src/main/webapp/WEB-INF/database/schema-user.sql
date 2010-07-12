create table Member (id identity,
					firstName varchar not null, 
					lastName varchar not null,
					email varchar(320) not null unique,
					password varchar not null,
					username varchar(15) unique,
					primary key (id));

create table App (consumerKey varchar,
				name varchar not null unique, 
				description varchar not null,
				website varchar not null unique,
				callbackUrl varchar,
				secret varchar not null,
				owner bigint not null,
				primary key (consumerKey),
				foreign key (owner) references Member(id));
					
create table ConnectedApp (accessToken varchar not null unique,
					app varchar,
					member bigint,
					secret varchar not null,
					primary key (app, member),
					foreign key (member) references Member(id),
					foreign key (app) references App(consumerKey));
					
create table ConnectedAccount (accessToken varchar not null,
					member bigint not null,
					accountName varchar,
					secret varchar not null,
					primary key (member, accountName),
					foreign key (member) references Member(id));
                    					
create table ResetPassword (token varchar,
					member bigint not null,
					primary key (token),
					foreign key (member) references Member(id));
					
create table OAuthToken (tokenValue varchar,
					member bigint,
					app varchar not null,
					secret varchar not null,
					callbackUrl varchar,
					updateTimestamp bigint not null,
					verifier varchar,
					primary key (tokenValue),
					foreign key (member) references Member(id),
					foreign key (app) references App(consumerKey));