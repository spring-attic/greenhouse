create table Invite (token varchar,
					email varchar unique not null,
					firstName varchar,
					lastName varchar,
					text varchar,
					sentTime timestamp not null default now(),
					sentBy bigint not null,
					primary key (token),
					foreign key (sentBy) references Member(id));

create table InviteAcceptAction (invite varchar not null,
					memberAction bigint unique not null,
					primary key (invite),
					foreign key (memberAction) references MemberAction(id),
					foreign key (invite) references Invite(token));