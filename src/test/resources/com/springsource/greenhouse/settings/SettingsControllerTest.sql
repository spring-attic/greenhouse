insert into Member (firstName, lastName, email, password, username, gender) values ('Keith' , 'Donald', 'youknow@vmware.com', 'whateveryouwantittobe', 'kdonald', 'M');
insert into App (consumerKey, name, description, website, callbackUrl, secret, owner) values ('a08318eb478a1ee31f69a55276f3af64', 'Greenhouse for the iPhone', 'Awesome', 'http://www.springsource.com', 'x-com-springsource-greenhouse://oauth-response', '80e7f8f7ba724aae9103f297e5fb9bdf', 1);
insert into ConnectedApp (accessToken, app, member, secret) values ('authme', 'a08318eb478a1ee31f69a55276f3af64', 1, 'nottelling');
