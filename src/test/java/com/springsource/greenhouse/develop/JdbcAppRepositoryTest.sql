insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Keith', 'Donald', 'kdonald@vmware.com', 'password', 'kdonald', 'M', '1977-12-01');

insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Craig', 'Walls', 'cwalls@vmware.com', 'password', 'habuma', 'M', '1977-12-01');
insert into App (name, slug, description, organization, website, apiKey, secret, callbackUrl) values ('Greenhouse for Facebook', 'greenhouse-for-facebook', 'Awesome', 'SpringSource', 'http://www.springsource.com', 'apikey1', 'secret1', null);
insert into AppDeveloper (app, member) values (1, 2);

insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Roy' , 'Clarkson', 'roy@vmware.com', 'password', 'roy', 'M', '1977-12-01');
insert into App (name, slug, description, organization, website, apiKey, secret, callbackUrl) values ('Greenhouse for the iPhone', 'greenhouse-for-the-iphone', 'Awesome', 'SpringSource', 'http://www.springsource.com', '76926d574e7ff5dabb94b5df23b6add6', '017c9bdbb1c08c870e4c0697ddc04abe', 'x-com-springsource-greenhouse://oauth-response');
insert into AppDeveloper (app, member) values (2, 3);
insert into App (name, slug, description, organization, website, apiKey, secret, callbackUrl) values ('Greenhouse for the Android', 'greenhouse-for-the-android', 'Awesome', 'SpringSource', 'http://www.springsource.com', 'apikey2', 'secret2', null);
insert into AppDeveloper (app, member) values (3, 3);

insert into AppConnection (app, member, accessToken, secret) values (2, 1, 'ea448a2cc81b84da29b3eeaf1e0242a1', '8d4c8b1930b25c4ea1a923fb9c213f4d');
