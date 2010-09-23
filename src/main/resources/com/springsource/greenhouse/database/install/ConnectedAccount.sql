create table AccountProvider (name varchar,
					apiKey varchar not null,					
					secret varchar not null,
					requestTokenUrl varchar not null,
					authorizeUrl varchar not null,
					accessTokenUrl varchar not null,
					primary key (name));

create table AccountConnection (member bigint,
					provider varchar,
					accessToken varchar not null,					
					accountId varchar,
					secret varchar, 
					primary key (member, provider),
					foreign key (member) references Member(id));