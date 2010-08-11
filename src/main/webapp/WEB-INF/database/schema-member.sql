create table Member (id identity,
					firstName varchar not null, 
					lastName varchar not null,
					email varchar(320) not null unique,
					password varchar not null,
					username varchar unique,
					pictureUrl varchar,
					reputation int default 0,
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
					
create table ConnectedApp (app varchar,
					member bigint,
					accessToken varchar not null unique,					
					secret varchar not null,
					primary key (app, member),
					foreign key (member) references Member(id),
					foreign key (app) references App(consumerKey));
					
create table ConnectedAccount (member bigint,
					provider varchar,
					accessToken varchar not null,					
					accountId varchar,
					secret varchar, 
					primary key (member, provider),
					foreign key (member) references Member(id));
create unique index AccessTokenKey on ConnectedAccount(provider, accessToken);
create unique index ProviderAccountKey on ConnectedAccount(provider, accountId); 

create table ResetPassword (token varchar,
					member bigint not null,
					primary key (token),
					foreign key (member) references Member(id));
					
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