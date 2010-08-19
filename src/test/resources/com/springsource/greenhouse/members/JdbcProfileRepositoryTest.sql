insert into Member (firstName, lastName, email, password, username, gender) values ('Craig' , 'Walls', 'whatever@vmware.com', 'letmein', 'habuma', 'M');
insert into Member (firstName, lastName, email, password, username, gender) values ('Roy' , 'Clarkson', 'roy@vmware.com', 'password', 'roy', 'M');

insert into ConnectedAccount (member, provider, accessToken, accountId) values (1, 'Twitter', 'twitter-token', 'habuma');
insert into ConnectedAccount (member, provider, accessToken, accountId) values (1, 'Facebook', 'facebook-token', '123456789');
