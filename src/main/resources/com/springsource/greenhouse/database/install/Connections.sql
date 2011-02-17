create table Connection (id identity,
						 accountId varchar not null,
						 providerId varchar not null,
						 accessToken varchar not null,                                   
						 secret varchar, 
						 refreshToken varchar,
						 providerAccountId varchar,
						 primary key (id));
create unique index AccessToken on Connection(accountId, providerId, accessToken);
