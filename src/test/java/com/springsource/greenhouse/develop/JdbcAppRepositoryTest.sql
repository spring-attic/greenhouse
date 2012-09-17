insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Keith', 'Donald', 'kdonald@vmware.com', 'password', 'kdonald', 'M', '1977-12-01');

insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Craig', 'Walls', 'cwalls@vmware.com', 'password', 'habuma', 'M', '1977-12-01');
insert into App (name, slug, description, organization, website, apiKey, secret, callbackUrl) values ('Greenhouse for Facebook', 'greenhouse-for-facebook', 'Awesome', 'SpringSource', 'http://www.springsource.com', 'apikey1', 'secret1', null);
insert into AppDeveloper (app, member) values (1, 2);

insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Roy' , 'Clarkson', 'roy@vmware.com', 'password', 'roy', 'M', '1977-12-01');
insert into App (name, slug, description, organization, website, apiKey, secret, callbackUrl) values ('Greenhouse for the iPhone', 'greenhouse-for-the-iphone', 'Awesome', 'SpringSource', 'http://www.springsource.com', '123456789', '987654321', 'x-com-springsource-greenhouse://oauth-response');
insert into AppDeveloper (app, member) values (2, 3);
insert into App (name, slug, description, organization, website, apiKey, secret, callbackUrl) values ('Greenhouse for the Android', 'greenhouse-for-the-android', 'Awesome', 'SpringSource', 'http://www.springsource.com', 'apikey2', 'secret2', null);
insert into AppDeveloper (app, member) values (3, 3);

