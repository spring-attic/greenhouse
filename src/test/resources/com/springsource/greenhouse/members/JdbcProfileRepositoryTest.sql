insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Craig' , 'Walls', 'whatever@vmware.com', 'letmein', 'habuma', 'M', '1977-12-01');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Roy' , 'Clarkson', 'roy@vmware.com', 'password', 'roy', 'M', '1977-12-01');

insert into ConnectedAccount (member, provider, accessToken, accountId) values (1, 'Twitter', 'twitter-token', 'habuma');
insert into ConnectedAccount (member, provider, accessToken, accountId) values (1, 'Facebook', 'facebook-token', '123456789');
