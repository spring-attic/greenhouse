insert into Member (firstName, lastName, email, password, username) values ('Craig', 'Walls', 'cwalls@vmware.com', 'password', 'habuma');
insert into Member (firstName, lastName, email, password, username) values ('Keith', 'Donald', 'kdonald@vmware.com', 'password', 'kdonald');
insert into ConnectedAccount (member, provider, accessToken, accountId) values (1, 'facebook', 'accesstoken', 1);
insert into ConnectedAccount (member, provider, accessToken) values (1, 'twitter', 'accesstoken');
insert into ConnectedAccount (member, provider, accessToken) values (1, 'linkedin', 'accesstoken');
insert into ConnectedAccount (member, provider, accessToken, accountId) values (2, 'facebook', 'accesstoken2', 2);