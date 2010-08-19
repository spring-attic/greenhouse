insert into Member (firstName, lastName, email, password, username, gender) values ('Keith' , 'Donald', 'kdonald@vmware.com', 'melbourne', 'kdonald', 'M');
insert into Member (firstName, lastName, email, password, username, gender) values ('Craig' , 'Walls', 'cwalls@vmware.com', 'plano', 'habuma', 'M');
insert into Member (firstName, lastName, email, password, username, gender) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson', 'M');

insert into MemberGroup (name, description, profileKey, hashtag, leader) values ('SpringOne2gx', 'The premier Spring Framework event', 'springone2gx', '#springone2gx', 1);

insert into Event (title, startTime, endTime, location, description, name, memberGroup) values ('Soon_Event', '2010-08-03 00:00:00Z', '2010-08-04 00:00:00Z', 'Chicago, IL', 'This event is soon', 'chitown', 1);
insert into Event (title, startTime, endTime, location, description, name, memberGroup) values ('Later_Event', '2011-04-27 00:00:00Z', '2011-04-30 00:00:00Z', 'Amsterdam', 'This event takes place next year', 'holland', 1);
insert into Event (title, startTime, endTime, location, description, name, memberGroup) values ('Old_Event', '2009-10-19 00:00:00Z', '2009-10-23 00:00:00Z', 'New Orleans, LA', 'This event is over', 'bigeasy', 1);

insert into EventTrack (event, code, name, description, chair) values (1, 'spr', 'Core Spring', 'The low-down on the core Spring Framework', 2);

insert into EventSession (event, number, title, startTime, endTime, description, hashtag, track) values (1, 1, 'Mastering MVC 3', '2010-08-03 14:00:00Z', '2010-08-03 15:00:00Z',
	'A deep-dive into the latest capabilities of MVC, Spring''s REST-ful web application development platform. ', '#spr101', 'spr');
insert into EventSessionLeader (event, session, leader) values (1, 1, 1);
insert into EventSessionLeader (event, session, leader) values (1, 1, 2);

insert into EventSession (event, number, title, startTime, endTime, description, hashtag, track) values (1, 2, 'Developing Social-Ready Web Applications', '2010-08-03 15:00:00Z', '2010-08-03 16:00:00Z', 
	'Businesses are increasingly recognizing the value of connecting with their customers on a more personal level....', '#spr102', 'spr');
insert into EventSessionLeader (event, session, leader) values (1, 2, 2);

insert into EventSession (event, number, title, startTime, endTime, description, hashtag, track) values (1, 3, 'Choices in Moble Application Development', '2010-10-21 18:45:00Z', '2010-10-21 20:15:00Z',
	'With the rising prevalence of advanced mobile platforms such as iPhone, Android, and Web OS, the desire for rich mobile clients as another means of accessing enterprise services is becoming something that can no longer be ignored. In this session, we will explore the current mobile development landscape and discuss what you as a Spring developer can do to support this increasingly important paradigm. We will examine the benefits and tradeoffs of native mobile client development vs. web-based mobile client development, and we will explore some of the emerging cross-platform options such as PhoneGap. We will look at the various strategies for utilizing a Spring back-end with these mobile platforms, such as consumption of RESTful services, authentication and authorization via OAuth, and server-push style messaging.',
	'#web202', 'spr');
insert into EventSessionLeader (event, session, leader) values (1, 3, 3);

insert into EventSessionFavorite (event, session, attendee) values (1, 1, 1);
insert into EventSessionFavorite (event, session, attendee) values (1, 1, 2);
insert into EventSessionFavorite (event, session, attendee) values (1, 1, 3);
insert into EventSessionFavorite (event, session, attendee) values (1, 2, 1);
insert into EventSessionFavorite (event, session, attendee) values (1, 2, 3);