insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Keith' , 'Donald', 'kdonald@vmware.com', 'melbourne', 'kdonald', 'M', '1977-12-01');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Craig' , 'Walls', 'cwalls@vmware.com', 'plano', 'habuma', 'M', '1977-12-01');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson', 'M', '1977-12-01');

insert into MemberGroup (name, slug, description, hashtag, leader) values ('SpringOne2gx', 'springone2gx', 'The premier Spring Framework event', '#s2gx', 1);

insert into Event (title, timezone, startTime, endTime, slug, description, memberGroup) values ('SpringOne2gx', 'America/Chicago', '2010-10-19 22:00:00Z', '2010-10-22 22:00:00Z', 'chicago', 'SpringOne 2GX is a one-of-a-kind conference for application developers, solution architects, web operations and IT teams who develop, deploy and manage business applications.', 1);

-- SpringOne Chicago 2010

insert into EventTrack (event, code, name, description, chair) values (1, 'spr', 'Essential Spring', 'Spring techniques and technologies applicable to most classes of applications', 2);
insert into EventTrack (event, code, name, description, chair) values (1, 'web', 'Web Application Development', 'What you need to know to build rich web applications', 1);

insert into EventSession (event, title, startTime, endTime, description, hashtag, track) values (1, 'Mastering MVC 3', '2010-10-20 17:45:00Z', '2010-10-20 19:15:00Z',
	'A deep-dive into the latest capabilities of Spring MVC, Spring''s REST-ful web application development platform.', '#mvc', 'web');
insert into EventSessionLeader (event, session, leader) values (1, 1, 1);

insert into EventSession (event, title, startTime, endTime, description, hashtag, track) values (1, 'Inside Web Flow 3 Development', '2010-10-20 19:45:00Z', '2010-10-20 21:15:00Z',
	'A look inside the development of Spring Web Flow 3, the next-generation version of Spring''s stateful controller framework for orchestrating multi-step user dialogs.', '#webflow', 'web');
insert into EventSessionLeader (event, session, leader) values (1, 2, 1);

insert into EventSession (event, title, startTime, endTime, description, hashtag, track) values (1, 'Developing Social-Ready Web Applications', '2010-10-21 17:45:00Z', '2010-10-21 19:15:00Z',
	'Businesses are increasingly recognizing the value of connecting with their customers on a more personal level. Companies can utilize social networking to transition from "Big Faceless Corporation" to "Friend" by taking their wares to the online communities where their customers are. In this age of social media, those communities are found at social network sites such as Facebook, Twitter, and LinkedIn. In this session, you''ll learn how to build Spring-based applications that interact with the various social networks. We''ll talk about new features in the Spring portfolio to support integration with social networks as well as how to start building social features into your own applications.',
	'#social', 'web');
insert into EventSessionLeader (event, session, leader) values (1, 3, 2);
	
insert into EventSession (event, title, startTime, endTime, description, hashtag, track) values (1, 'Choices in Mobile Application Development', '2010-10-21 19:45:00Z', '2010-10-21 21:15:00Z',
	'With the rising prevalence of advanced mobile platforms such as iPhone, Android, and Web OS, the desire for rich mobile clients as another means of accessing enterprise services is becoming something that can no longer be ignored. In this session, we will explore the current mobile development landscape and discuss what you as a Spring developer can do to support this increasingly important paradigm. We will examine the benefits and tradeoffs of native mobile client development vs. web-based mobile client development, and we will explore some of the emerging cross-platform options such as PhoneGap. We will look at the various strategies for utilizing a Spring back-end with these mobile platforms, such as consumption of RESTful services, authentication and authorization via OAuth, and server-push style messaging.',
	'#mobile', 'web');
insert into EventSessionLeader (event, session, leader, rank) values (1, 4, 3, 1);
insert into EventSessionLeader (event, session, leader, rank) values (1, 4, 1, 2);

insert into EventSessionFavorite (event, session, attendee, rank) values (1, 3, 1, 1);
insert into EventSessionFavorite (event, session, attendee, rank) values (1, 4, 1, 2);
