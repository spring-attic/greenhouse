insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson', 'M', '1977-12-01');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Craig', 'Walls', 'cwalls@vmware.com', 'plano', 'habuma', 'M', '1977-12-01');

insert into Invite (token, firstName, lastName, email, sentBy) values ('abc', 'Keith', 'Donald', 'keith.donald@springsource.com', 1);

insert into Invite (token, firstName, lastName, email, sentBy) values ('def', 'Craig', 'Walls', 'cwalls@vmware.com', 1);
insert into MemberAction (actionType, performTime, member) values ('InviteAccept', '2010-10-18 22:00:00Z', 2);
insert into InviteAcceptAction (invite, memberAction) values ('def', 1);