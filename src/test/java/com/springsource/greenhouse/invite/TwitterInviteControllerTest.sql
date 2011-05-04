insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Craig', 'Walls', 'cwalls@vmware.com', 'password', 'habuma', 'M', '1977-12-01');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Keith', 'Donald', 'kdonald@vmware.com', 'password', 'kdonald', 'M', '1977-12-01');
insert into AccountProvider (name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl) values ('twitter', 'whatev', 'secret', 'http://www.twitter.com', 'http://www.twitter.com', 'http://www.twitter.com');
insert into AccountConnection (member, accountId, provider, accessToken) values (1, 'habuma', 'twitter', 'accesstoken');
