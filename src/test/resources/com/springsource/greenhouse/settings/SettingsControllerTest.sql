insert into User (firstName, lastName, email, password, username) values ('Keith' , 'Donald', 'youknow@vmware.com', 'whateveryouwantittobe', 'kdonald');
insert into Consumer (consumerKey, name, description, website, callbackUrl, secret, ownerId) values ('a08318eb478a1ee31f69a55276f3af64', 'Greenhouse for the iPhone', 'Awesome', 'http://www.springsource.com', 'x-com-springsource-greenhouse://oauth-response', '80e7f8f7ba724aae9103f297e5fb9bdf', 1);
insert into AuthorizedConsumer (userId, consumerKey, accessToken, secret) values (1, 'a08318eb478a1ee31f69a55276f3af64', 'authme', 'nottelling');
