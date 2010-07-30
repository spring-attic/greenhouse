insert into Member (firstName, lastName, email, password, username) values ('Keith' , 'Donald', 'kdonald@vmware.com', 'melbourne', 'kdonald');
insert into Member (firstName, lastName, email, password, username) values ('Craig' , 'Walls', 'cwalls@vmware.com', 'plano', 'habuma');
insert into Member (firstName, lastName, email, password, username) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson');

insert into MemberGroup (name, description, profileKey, hashtag, leader) values ('SpringOne2gx', 'The premier Spring Framework event', 'springone2gx', '#springone2gx', 1);

insert into Event (title, startTime, endTime, location, description, name, memberGroup) values ('Later_Event', '2011-04-27', '2011-04-29', 'Amsterdam', 'This event takes place next year', 'holland', 1);
insert into Event (title, startTime, endTime, location, description, name, memberGroup) values ('Soon_Event', '2010-08-27', '2010-08-30', 'Chicago, IL', 'This event is soon', 'chitown', 1);
insert into Event (title, startTime, endTime, location, description, name, memberGroup) values ('Old_Event', '2009-10-19', '2009-10-22', 'New Orleans, LA', 'This event is over', 'bigeasy', 1);

insert into EventTrack (event, code, name, description, chair) values (2, 'spr', 'Core Spring', 'The low-down on the core Spring Framework', 2);

insert into EventSession (event, number, title, startTime, endTime, description, hashtag, track) values (2, 1, 'Mastering MVC 3', '2010-08-27 14:00:00', '2010-08-27 15:00:00',
	'A deep-dive into the latest capabilities of MVC, Spring''s REST-ful web application development platform. ', '#spr101', 'spr');
insert into EventSessionLeader (event, session, leader) values (2, 1, 1);
insert into EventSessionLeader (event, session, leader) values (2, 1, 2);

insert into EventSession (event, number, title, startTime, endTime, description, hashtag, track) values (2, 2, 'Developing Social-Ready Web Applications', '2010-08-27 15:00:00', '2010-08-27 16:00:00', 
	'Businesses are increasingly recognizing the value of connecting with their customers on a more personal level....', '#spr102', 'spr');
insert into EventSessionLeader (event, session, leader) values (2, 2, 2);
