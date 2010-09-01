create table ConnectedAccount (member bigint,
					provider varchar,
					accessToken varchar not null,					
					accountId varchar,
					secret varchar, 
					primary key (member, provider),
					foreign key (member) references Member(id));
create unique index AccessTokenKey on ConnectedAccount(provider, accessToken);
create unique index ProviderAccountKey on ConnectedAccount(provider, accountId);