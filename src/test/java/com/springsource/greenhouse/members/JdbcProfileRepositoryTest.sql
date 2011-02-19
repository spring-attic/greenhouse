insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Craig' , 'Walls', 'whatever@vmware.com', 'letmein', 'habuma', 'M', '1977-12-01');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Roy' , 'Clarkson', 'roy@vmware.com', 'password', 'roy', 'M', '1977-12-01');

insert into ServiceProvider (name, displayName, implementation, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl) values ('twitter', 'Twitter', 'whatev', 'whatev', 'secret', 'http://www.twitter.com', 'http://www.twitter.com', 'http://www.twitter.com');
insert into ServiceProvider (name, displayName, implementation, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl) values ('facebook', 'Facebook', 'whatev', 'whatev', 'secret', 'http://www.facebook.com', 'http://www.facebook.com', 'http://www.facebook.com');

insert into AccountConnection (member, provider, accessToken, accountId, profileUrl) values (1, 'twitter', 'twitter-token', 'habuma', 'http://www.twitter.com/habuma');
insert into AccountConnection (member, provider, accessToken, accountId, profileUrl) values (1, 'facebook', 'facebook-token', '123456789', 'http://www.facebook.com/profile.php?id=123456789');
