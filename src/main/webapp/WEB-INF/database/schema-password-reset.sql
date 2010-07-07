create table PasswordResetRequest ( requestKey varchar not null unique,
					userId bigint not null,
					primary key (requestKey),
					foreign key (userId) references User(id));
