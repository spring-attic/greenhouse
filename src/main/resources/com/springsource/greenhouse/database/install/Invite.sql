create table Invite (token varchar,
					email varchar unique not null,
					firstName varchar,
					lastName varchar,
					text varchar,
					sentTime timestamp not null default now(),
					sentBy bigint not null,
					primary key (token),
					foreign key (sentBy) references Member(id));

create table AcceptInviteAction (memberAction bigint, 
					sentBy bigint not null,
					primary key (memberAction),
					foreign key (memberAction) references MemberAction(id),
					foreign key (sentBy) references Member(id));