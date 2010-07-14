insert into Member (firstName, lastName, email, password, username) values ('Keith' , 'Donald', 'kdonald@vmware.com', 'melbourne', 'kdonald');
insert into Member (firstName, lastName, email, password, username) values ('Craig' , 'Walls', 'cwalls@vmware.com', 'plano', 'habuma');
insert into Member (firstName, lastName, email, password, username) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson');
insert into MemberGroup (name, description, hashtag, leader) values ('SpringOne', 'The premier Spring Framework event.', '#springone', 1);

insert into Event (title, description, startTime, endTime, location, memberGroup, hashtag) values (
	'Later Event', 'This event takes place next year', '2011-04-27', '2011-04-29', 'Amsterdam', 1, '#future');
insert into Event (title, description, startTime, endTime, location, memberGroup, hashtag) values (
	'Soon Event', 'This event is soon', '2010-10-19', '2010-10-22', 'Chicago, IL', 1, '#soon');
insert into Event (title, description, startTime, endTime, location, memberGroup, hashtag) values (
	'Old Event', 'This event is over', '2009-10-19', '2009-10-22', 'New Orleans, LA', 1, '#old');


insert into EventTrack (name, description, chair, event) values ('Core Spring', 'The low-down on the core Spring Framework', 1, 2);

insert into EventSession (code, title, description, startTime, endTime, speaker, event, track, hashtag) values ('CS1', 
	'Developing Social-Ready Web Applications', 
	'Businesses are increasingly recognizing the value of connecting with their customers on a more personal level....', 
	'2010-10-20', '2010-10-20', 2, 2, 1, '#s12gxcs1');

insert into EventSession (code, title, description, startTime, endTime, speaker, event, track, hashtag) values ('CS2', 
	'Mastering MVC 3', 
	'A deep-dive into the latest capabilities of MVC, Spring''s REST-ful web application development platform. ', 
	'2010-10-19', '2010-10-19', 1, 2, 1, '#s12gxcs1');