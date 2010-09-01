create table App (consumerKey varchar,
				name varchar not null unique, 
				description varchar not null,
				website varchar not null unique,
				callbackUrl varchar,
				secret varchar not null,
				owner bigint not null,
				primary key (consumerKey),
				foreign key (owner) references Member(id));
					
create table ConnectedApp (app varchar,
					member bigint,
					accessToken varchar not null unique,					
					secret varchar not null,
					primary key (app, member),
					foreign key (member) references Member(id),
					foreign key (app) references App(consumerKey));
					
create table OAuthToken (tokenValue varchar,
					app varchar not null,
					member bigint,
					secret varchar not null,
					callbackUrl varchar,
					updateTimestamp bigint not null,
					verifier varchar,
					primary key (tokenValue),
					foreign key (member) references Member(id),
					foreign key (app) references App(consumerKey));