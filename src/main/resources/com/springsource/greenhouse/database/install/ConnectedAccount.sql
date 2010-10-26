create table ServiceProvider (name varchar,
                    displayName varchar not null,
                   	implementation varchar not null,
					apiKey varchar not null,					
					secret varchar,
					appId bigint,
					requestTokenUrl varchar,
					authorizeUrl varchar,
					accessTokenUrl varchar,
					primary key (name));

create table AccountConnection (member bigint,
					provider varchar,
					accessToken varchar not null,					
					secret varchar, 
					accountId varchar,	
					profileUrl varchar,
					primary key (member, provider, accessToken),
					foreign key (member) references Member(id),
					foreign key (provider) references ServiceProvider(name));
create index ProviderAccountKey on AccountConnection(accountId);