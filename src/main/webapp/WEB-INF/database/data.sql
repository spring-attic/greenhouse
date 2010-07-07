insert into User (firstName, lastName, email, password, username) values ('Keith' , 'Donald', 'kdonald@vmware.com', 'melbourne', 'kdonald');
insert into User (firstName, lastName, email, password, username) values ('Craig' , 'Walls', 'cwalls@vmware.com', 'plano', 'habuma');
insert into User (firstName, lastName, email, password, username) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson');

insert into Consumer (consumerKey, name, description, website, callbackUrl, secret, ownerId) values ('a08318eb478a1ee31f69a55276f3af64', 'Greenhouse for the iPhone', 'Awesome', 'http://www.springsource.com', 'x-com-springsource-greenhouse://oauth-response', '80e7f8f7ba724aae9103f297e5fb9bdf', 2);

insert into Update (text, updateTimestamp, userId) values ('Keith Donald signed up', 1278019179970, 1);
insert into Update (text, updateTimestamp, userId) values ('Craig Walls signed up', 1278017173970, 2);
insert into Update (text, updateTimestamp, userId) values ('Roy Clarkson signed up', 1274015177470, 3);
