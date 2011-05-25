drop table UserConnection if exists;
create table UserConnection (userId varchar not null,
					providerId varchar not null,
					providerUserId varchar,
					rank int not null,
					displayName varchar,
					profileUrl varchar,
					imageUrl varchar,
					accessToken varchar not null,					
					secret varchar,
					refreshToken varchar,
					expireTime bigint,
					primary key (userId, providerId, providerUserId));
create unique index UserConnectionRank on UserConnection(userId, providerId, rank);