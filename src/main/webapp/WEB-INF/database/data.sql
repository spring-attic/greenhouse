insert into Member (firstName, lastName, email, password, username) values ('Keith' , 'Donald', 'kdonald@vmware.com', 'melbourne', 'kdonald');
insert into Member (firstName, lastName, email, password, username) values ('Craig' , 'Walls', 'craig@habuma.com', 'plano', 'habuma');
insert into Member (firstName, lastName, email, password, username) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson');
insert into Member (firstName, lastName, email, password, username) values ('Jeremy', 'Grelle', 'jgrelle@vmware.com', 'churchkeys', 'jgrelle');

insert into App (consumerKey, name, description, website, callbackUrl, secret, owner) values ('a08318eb478a1ee31f69a55276f3af64', 'Greenhouse for the iPhone', 'Awesome', 'http://www.springsource.com', 'x-com-springsource-greenhouse://oauth-response', '80e7f8f7ba724aae9103f297e5fb9bdf', 2);

insert into Update (text, updateTimestamp, member) values ('Keith Donald signed up', 1278019179970, 1);
insert into Update (text, updateTimestamp, member) values ('Craig Walls signed up', 1278017173970, 2);
insert into Update (text, updateTimestamp, member) values ('Roy Clarkson signed up', 1274015177470, 3);

insert into MemberGroup (name, description, profileKey, hashtag, leader) values ('SpringOne2gx', 'The premier event for Spring technologies', 'springone2gx', '#springone2gx', 1);

insert into Event (title, startDate, endDate, location, description, memberGroup) values ('SpringOne/2GX', '2010-10-19', '2010-10-22', 'SpringOne 2GX is a one-of-a-kind conference for application developers, solution architects, web operations and IT teams who develop, deploy and manage business applications.', 'Chicago, IL', 1);

insert into EventTrack (name, description, chair, event) values ('Essential Spring', 'Spring techniques and technologies applicable to most classes of applications', 2, 1);
insert into EventTrack (name, description, chair, event) values ('Web Application Development', 'What you need to know to build rich web applications', 1, 1);

insert into EventSession (code, title, startTime, endTime, description, event, track) values (201, 
	'Mastering MVC 3', '2010-10-20 16:45:00', '2010-10-20 18:15:00',
	'A deep-dive into the latest capabilities of Spring MVC, Spring%27s REST-ful web application development platform.', 1, 2);
insert into EventSessionLeader (session, leader) values (201, 1);

insert into EventSession (code, title, startTime, endTime, description, event, track) values (202, 
	'Inside Web Flow 3 Development', '2010-10-20 18:45:00', '2010-10-20 20:15:00',
	'A look inside the development of Spring Web Flow 3, the next-generation version of Spring%27s stateful controller framework for orchestrating multi-step user dialogs.', 1, 2);
insert into EventSessionLeader (session, leader) values (202, 1);

insert into EventSession (code, title, startTime, endTime, description, event, track) values (203, 
	'Developing Social-Ready Web Applications', '2010-10-21 16:45:00', '2010-10-21 18:15:00',
	'Businesses are increasingly recognizing the value of connecting with their customers on a more personal level. Companies can utilize social networking to transition from "Big Faceless Corporation" to "Friend" by taking their wares to the online communities where their customers are. In this age of social media, those communities are found at social network sites such as Facebook, Twitter, and LinkedIn. In this session, you%27ll learn how to build Spring-based applications that interact with the various social networks. We%27ll talk about new features in the Spring portfolio to support integration with social networks as well as how to start building social features into your own applications.',
	1, 2);
insert into EventSessionLeader (session, leader) values (203, 2);
	
insert into EventSession (code, title, startTime, endTime, description, event, track) values (204, 
	'Choices in Moble Application Development', '2010-10-21 18:45:00', '2010-10-21 20:15:00',
	'With the rising prevalence of advanced mobile platforms such as iPhone, Android, and Web OS, the desire for rich mobile clients as another means of accessing enterprise services is becoming something that can no longer be ignored. In this session, we will explore the current mobile development landscape and discuss what you as a Spring developer can do to support this increasingly important paradigm. We will examine the benefits and tradeoffs of native mobile client development vs. web-based mobile client development, and we will explore some of the emerging cross-platform options such as PhoneGap. We will look at the various strategies for utilizing a Spring back-end with these mobile platforms, such as consumption of RESTful services, authentication and authorization via OAuth, and server-push style messaging.',
	1, 2);
insert into EventSessionLeader (session, leader) values (204, 3);
insert into EventSessionLeader (session, leader) values (204, 4);