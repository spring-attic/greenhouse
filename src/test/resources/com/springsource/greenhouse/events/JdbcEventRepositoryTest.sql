insert into Member (firstName, lastName, email, password, username) values ('Keith' , 'Donald', 'kdonald@vmware.com', 'melbourne', 'kdonald');
insert into Member (firstName, lastName, email, password, username) values ('Craig' , 'Walls', 'cwalls@vmware.com', 'plano', 'habuma');
insert into Member (firstName, lastName, email, password, username) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson');

insert into MemberGroup (name, description, profileKey, searchString, leader) values ('SpringOne2gx', 'The premier Spring Framework event', 'springone2gx', '#springone2gx', 1);

insert into Event (title, startDate, endDate, location, description, memberGroup) values ('Later_Event', '2011-04-27', '2011-04-29', 'Amsterdam', 'This event takes place next year', 1);
insert into Event (title, startDate, endDate, location, description, memberGroup) values ('Soon_Event', '2010-07-27', '2010-07-30', 'Chicago, IL', 'This event is soon', 1);
insert into Event (title, startDate, endDate, location, description, memberGroup) values ('Old_Event', '2009-10-19', '2009-10-22', 'New Orleans, LA', 'This event is over', 1);

insert into EventTrack (name, description, chair, event) values ('Core Spring', 'The low-down on the core Spring Framework', 1, 2);

insert into EventSession (code, title, startTime, endTime, description, event, track) values (201, 'Developing Social-Ready Web Applications', '2010-10-20 14:00:00', '2010-10-20 15:00:00', 
	'Businesses are increasingly recognizing the value of connecting with their customers on a more personal level....', 2, 1);
insert into EventSessionLeader (session, leader) values (201, 2);

insert into EventSession (code, title, startTime, endTime, description, event, track) values (202, 'Mastering MVC 3', '2010-10-19 14:00:00', '2010-10-19 15:00:00',
	'A deep-dive into the latest capabilities of MVC, Spring''s REST-ful web application development platform. ', 2, 1);
insert into EventSessionLeader (session, leader) values (202, 1);
insert into EventSessionLeader (session, leader) values (202, 2);
