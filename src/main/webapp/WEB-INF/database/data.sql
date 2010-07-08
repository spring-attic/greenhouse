insert into User (firstName, lastName, email, password, username) values ('Keith' , 'Donald', 'kdonald@vmware.com', 'melbourne', 'kdonald');
insert into User (firstName, lastName, email, password, username) values ('Craig' , 'Walls', 'cwalls@vmware.com', 'plano', 'habuma');
insert into User (firstName, lastName, email, password, username) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson');

insert into Consumer (consumerKey, name, description, website, callbackUrl, secret, ownerId) values ('a08318eb478a1ee31f69a55276f3af64', 'Greenhouse for the iPhone', 'Awesome', 'http://www.springsource.com', 'x-com-springsource-greenhouse://oauth-response', '80e7f8f7ba724aae9103f297e5fb9bdf', 2);

insert into Update (text, updateTimestamp, userId) values ('Keith Donald signed up', 1278019179970, 1);
insert into Update (text, updateTimestamp, userId) values ('Craig Walls signed up', 1278017173970, 2);
insert into Update (text, updateTimestamp, userId) values ('Roy Clarkson signed up', 1274015177470, 3);

insert into PasswordResetRequest (requestKey, userId) values ('booger', 2);


insert into Event (title, description, startDate, hashtag, createdByUserId, modifiedByUserId, lastModified) 
values ('Atlanta Spring User Group', 'Cool place to hang out and eat pizza', '2010-08-15', '#atlspring', 3, 3, current_timestamp());

insert into Event (title, description, startDate, endDate, hashtag, createdByUserId, modifiedByUserId, lastModified) 
values ('SpringOne 2GX', 'Get your learning on', '2010-10-19', '2010-10-22', '#SpringOne2GX', 1, 1, current_timestamp());

insert into EventSession (eventId, title, description, sessionDate, startTime, endTime, hashtag, createdByUserId, modifiedByUserId, lastModified) 
values (2, 'Spring and Social', 'I bet you want to tweet from your Spring app, right?', '2010-10-20', '13:00:00', '14:30:00', '#SpringOne2GX', 2, 2, current_timestamp());

insert into EventSession (eventId, title, description, sessionDate, startTime, endTime, hashtag, createdByUserId, modifiedByUserId, lastModified) 
values (2, 'Spring and Mobile', 'All about the apps!', '2010-10-21', '09:30:00', '10:00:00', '#SpringOne2GX', 3, 3, current_timestamp());
