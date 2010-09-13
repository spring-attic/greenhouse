insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Craig' , 'Walls', 'cwalls@vmware.com', 'plano', 'habuma', 'M', '1977-12-01');

insert into AccountProvider (name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl) values ('twitter', 'whatev', 'secret', 'http://www.twitter.com', 'http://www.twitter.com', 'http://www.twitter.com');
insert into AccountProvider (name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl) values ('myspace', 'whatev', 'secret', 'http://www.twitter.com', 'http://www.twitter.com', 'http://www.twitter.com');

insert into AccountConnection (member, provider, accessToken, secret) values (1, 'twitter', 'twitterToken', 'twitterTokenSecret');