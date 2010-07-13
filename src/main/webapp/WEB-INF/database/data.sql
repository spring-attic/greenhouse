insert into Member (firstName, lastName, email, password, username) values ('Keith' , 'Donald', 'kdonald@vmware.com', 'melbourne', 'kdonald');
insert into Member (firstName, lastName, email, password, username) values ('Craig' , 'Walls', 'cwalls@vmware.com', 'plano', 'habuma');
insert into Member (firstName, lastName, email, password, username) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson');

insert into App (consumerKey, name, description, website, callbackUrl, secret, owner) values ('a08318eb478a1ee31f69a55276f3af64', 'Greenhouse for the iPhone', 'Awesome', 'http://www.springsource.com', 'x-com-springsource-greenhouse://oauth-response', '80e7f8f7ba724aae9103f297e5fb9bdf', 2);

insert into Update (text, updateTimestamp, member) values ('Keith Donald signed up', 1278019179970, 1);
insert into Update (text, updateTimestamp, member) values ('Craig Walls signed up', 1278017173970, 2);
insert into Update (text, updateTimestamp, member) values ('Roy Clarkson signed up', 1274015177470, 3);

insert into MemberGroup (name, description, hashtag, leader) values ('SpringSource', 'This is where the Spring Groovy stuff goes down.', '#spring', 1);

insert into Event (title, description, startTime, endTime, location, memberGroup, hashtag) values ('SpringOne/2GX', 'Come hear Spring experts lay down some mad Spring skillz.', '2010-10-19', '2010-10-22', 'Chicago, IL', 1, '#springone2gx');

insert into EventTrack (name, description, chair, event) values ('Core Spring', 'The low-down on the core Spring Framework', 1, 1);

insert into EventSession (code, title, description, startTime, endTime, speaker, event, track, hashtag) values ('CS1', 'Coming in Spring 3.1', 'Come see what''s coming up in Spring 3.1', '2010-10-19', '2010-10-19', 2, 1, 1, '#s12gxcs1');
