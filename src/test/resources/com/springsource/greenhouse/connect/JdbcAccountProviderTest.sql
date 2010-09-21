insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Craig', 'Walls', 'cwalls@vmware.com', 'password', 'habuma', 'M', '1977-12-01');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Keith', 'Donald', 'kdonald@vmware.com', 'password', 'kdonald', 'M', '1977-12-01');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'password', 'rclarkson', 'M', '1977-12-01');
--insert into AccountProvider (name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl) values ('twitter', 'whatev', 'secret', 'http://www.twitter.com', 'http://www.twitter.com', 'http://www.twitter.com');
insert into AccountProvider (name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl) values ('twitter', 'QLM4vX68dBRLG1sE7zSJiA', '4Ah41ojlwi249GCI1UU5oKolmNDpjLLXgPKwGfVs0', 'https://twitter.com/oauth/request_token', 'https://twitter.com/oauth/authorize', 'https://twitter.com/oauth/access_token');

insert into AccountConnection (member, accountId, provider, accessToken) values (1, 'habuma', 'twitter', 'accesstoken');
insert into AccountConnection (member, accountId, provider, accessToken) values (3, 'rclarkson', 'twitter', 'anothertoken');
