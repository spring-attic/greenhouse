create table App (id identity,
				name varchar not null unique, 
				slug varchar not null unique,
				description varchar not null,
				organization varchar,
				website varchar,
				apiKey varchar unique,
				secret varchar not null unique,
				callbackUrl varchar,
				primary key (id));

create table AppDeveloper (app bigint,
				member bigint, 
				primary key (app, member),
				foreign key (app) references App(id) on delete cascade,				
				foreign key (member) references Member(id));

create table AppConnection (app varchar,
				member bigint,
				accessToken varchar not null unique,					
				secret varchar not null,
				primary key (app, member),
				foreign key (member) references Member(id),
				foreign key (app) references App(id) on delete cascade);
					